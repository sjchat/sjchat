package sjchat.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import sjchat.daos.ChatDao;
import sjchat.daos.ChatDaoImpl;
import sjchat.daos.MessageDao;
import sjchat.daos.MessageDaoImpl;
import sjchat.entities.ChatEntity;
import sjchat.entities.MessageEntity;
import sjchat.exceptions.NoEntityExistsException;
import sjchat.queue.MessageExchange;
import sjchat.queue.QueueException;
import sjchat.queue.producer.MessageProducer;
import sjchat.users.GetUserRequest;
import sjchat.users.UserServiceGrpc;

class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
  MessageDao messageDao = new MessageDaoImpl();
  ChatDao chatDao = new ChatDaoImpl();
  private Channel userServiceChannel = buildUserServiceChannel();
  private MessageExchange messageExchange;

  public MessageService() throws Exception {
    new Thread(new Runnable() {
      public void run() {
        initializeMessageExchange();
      }
    }).start();
  }

  private static Channel buildUserServiceChannel() {
    String host = System.getenv("USER_SERVICE_HOST");
    host = (host == null) ? "localhost" : host;

    return ManagedChannelBuilder.forAddress(host, 50051).usePlaintext(true).build();
  }

  private void initializeMessageExchange() {
    try {
      tryInitializeMessageExchange();
    } catch (Exception e) {
      System.out.println("Could not initialize message exchange");
      System.out.println(e.toString());
    }
  }

  private void tryInitializeMessageExchange() throws Exception {
    int attempts = 0;
    int maxAttempts = 5;
    int timeSleep = 10;
    messageExchange = new MessageExchange();

    while (attempts < maxAttempts) {
      try {
        messageExchange.initialize();
        break;
      } catch (QueueException exception) {
        attempts++;
        if (attempts < maxAttempts) {
          System.out.println("Could not initialize message exchange");
          System.out.println(exception.toString());
          System.out.println("Retrying in " + timeSleep + " seconds.");
          Thread.sleep(timeSleep * 1000);
        } else {
          throw exception;
        }
      }
    }
  }

  public Chat buildChat(ChatEntity entity) {
    Chat.Builder builder = Chat.newBuilder().setId(entity.getId()).setTitle(entity.getTitle());
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel);
    for (String userId : entity.getParticipants()) {
      builder.addParticipants(blockingStub.getUser(GetUserRequest.newBuilder().setId(userId).build()).getUser());
    }
    return builder.build();
  }

  @Override
  public void getChatList(GetChatListRequest req, StreamObserver<GetChatListResponse> responseObserver) {
    GetChatListResponse.Builder chatListResponseBuilder = GetChatListResponse.newBuilder();

    List<ChatEntity> chatEntityList = chatDao.findAll();
    for (ChatEntity entity : chatEntityList) {
      chatListResponseBuilder.addChats(buildChat(entity));
    }

    GetChatListResponse chatResponse = chatListResponseBuilder.build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getChat(GetChatRequest req, StreamObserver<GetChatResponse> responseObserver) {
    ChatEntity entity = chatDao.find(req.getId());
    GetChatResponse chatResponse;
    if (entity == null) {
      chatResponse = GetChatResponse.newBuilder().setChat(Chat.newBuilder().setId("null").setTitle("Not existing")).build();
    } else {
      chatResponse = GetChatResponse.newBuilder().setChat(buildChat(entity)).build();
    }


    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void createChat(CreateChatRequest req, StreamObserver<CreateChatResponse> responseObserver) {
    ChatEntity.Builder chatBuilder = new ChatEntity.Builder();
    chatBuilder.setId(null);
    chatBuilder.setTitle(req.getTitle());
    chatBuilder.setParticipants(req.getParticipantsList());

    ChatEntity entity = chatBuilder.build();
    chatDao.create(entity);

    CreateChatResponse chatResponse = CreateChatResponse.newBuilder().setChat(buildChat(entity)).build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void updateChat(UpdateChatRequest req, StreamObserver<UpdateChatResponse> responseObserver) {
    ChatEntity.Builder builder = new ChatEntity.Builder();
    builder.setId(req.getId())
            .setTitle(req.getTitle())
            .setParticipants(req.getParticipantsList());

    ChatEntity entity = builder.build();
    try {
      entity = chatDao.update(entity);
    } catch (NoEntityExistsException e) {
      System.out.println("No chat with id: " + req.getId() + " exists");
    }

    UpdateChatResponse chatResponse = UpdateChatResponse.newBuilder().setChat(buildChat(entity)).build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getMessages(GetMessagesRequest req, StreamObserver<GetMessagesResponse> responseObserver) {
    GetMessagesResponse.Builder messageListResponseBuilder = GetMessagesResponse.newBuilder();

    List<MessageEntity> messageEntities = messageDao.getInChat(req.getChatId());

    for (MessageEntity entity : messageEntities) {
      messageListResponseBuilder.addMessages(messageFromMessageEntity(entity));
    }

    Collections.sort(messageEntities, new Comparator<MessageEntity>() {
      public int compare(MessageEntity lhs, MessageEntity rhs) {
        return Long.compare(lhs.getCreatedAt(), rhs.getCreatedAt());
      }
    });

    responseObserver.onNext(messageListResponseBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void sendMessage(SendMessageRequest req, StreamObserver<SendMessageResponse> responseObserver) {
    try{
      addParticipant(req.getChatId(), req.getSender());
    }catch(NoEntityExistsException e){
      responseObserver.onError(new Exception("No chat with that id exists"));
      return;
    }

    MessageEntity entity = new MessageEntity(null, req.getChatId(), req.getMessage(), req.getSender(), System.currentTimeMillis());
    messageDao.create(entity);

    Message message = messageFromMessageEntity(entity);

    SendMessageResponse messageResponse = SendMessageResponse.newBuilder().setMessage(message).build();

    responseObserver.onNext(messageResponse);

    MessageProducer producer = new MessageProducer(messageExchange);
    try {
      producer.dispatchMessage(message);
    } catch (QueueException exception) {
      System.out.println("Could not dispatch message to message exchange");
    }

    responseObserver.onCompleted();
  }

  private void addParticipant(String chatId, String participantId) throws NoEntityExistsException{
    ChatEntity entity = chatDao.find(chatId);
    List<String> participants = new ArrayList<String>();
    participants.addAll(entity.getParticipants());
    if(participants.indexOf(participantId) != -1){
      return;
    }
    participants.add(participantId);
    entity = new ChatEntity.Builder()
            .setId(entity.getId())
            .setTitle(entity.getTitle())
            .setParticipants(participants).build();
    chatDao.update(entity);
  }

  public Message messageFromMessageEntity(MessageEntity entity){
    return Message.newBuilder()
            .setId(entity.getId())
            .setMessage(entity.getMessage())
            .setSender(entity.getSender())
            .setChat(entity.getChatid())
            .setCreatedAt(entity.getCreatedAt()).build();
  }
}
