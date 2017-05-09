package sjchat.messages;

import java.util.List;
import java.util.Random;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import sjchat.daos.ChatDao;
import sjchat.daos.ChatDaoImpl;
import sjchat.daos.MessageDao;
import sjchat.daos.MessageDaoImpl;
import sjchat.entities.ChatEntity;
import sjchat.users.GetUserRequest;
import sjchat.users.User;
import sjchat.users.UserServiceGrpc;

class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
  MessageDao messageDao = new MessageDaoImpl();
  ChatDao chatDao = new ChatDaoImpl();
  private Channel userServiceChannel = buildUserServiceChannel();

  private static Channel buildUserServiceChannel() {
    String host = System.getenv("USER_SERVICE_HOST");
    host = (host == null) ? "localhost" : host;

    return ManagedChannelBuilder.forAddress(host, 50051).usePlaintext(true).build(); //TODO: Put port in config file
  }
  private static Chat.Builder buildMockChat() {
    Random random = new Random();
    Chat.Builder chatBuilder = Chat.newBuilder();
    chatBuilder.setId("mock-id");
    chatBuilder.setTitle("Test chat " + chatBuilder.getId());
    return chatBuilder;
  }

  private static User.Builder buildMockUser() {
    Random random = new Random();
    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId("mock-id");
    userBuilder.setUsername("user_" + userBuilder.getId());
    return userBuilder;
  }

  private static Message.Builder buildMockMessage() {
    Random random = new Random();
    Message.Builder messageBuilder = Message.newBuilder();
    messageBuilder.setId("mock-id");
    messageBuilder.setMessage("Test message " + messageBuilder.getId());
    messageBuilder.setSender("id");
    return messageBuilder;
  }

  public Chat buildChat(ChatEntity entity){
    Chat.Builder builder = Chat.newBuilder().setId(entity.getId()).setTitle(entity.getTitle());
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel);
    for(String userId : entity.getParticipants()){
      builder.addParticipants(blockingStub.getUser(GetUserRequest.newBuilder().setId(userId).build()).getUser());
    }
    return builder.build();
  }

  @Override
  public void getChatList(GetChatListRequest req, StreamObserver<GetChatListResponse> responseObserver) {
    GetChatListResponse.Builder chatListResponseBuilder = GetChatListResponse.newBuilder();

    List<ChatEntity> chatEntityList = chatDao.findAll();
    for(ChatEntity entity : chatEntityList){
      chatListResponseBuilder.addChats(buildChat(entity));
    }

    GetChatListResponse chatResponse = chatListResponseBuilder.build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getChat(GetChatRequest req, StreamObserver<GetChatResponse> responseObserver) {
    ChatEntity entity = chatDao.find(req.getId());

    GetChatResponse chatResponse = GetChatResponse.newBuilder().setChat(buildChat(entity)).build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void createChat(CreateChatRequest req, StreamObserver<CreateChatResponse> responseObserver) {
    Random random = new Random();

    Chat.Builder chatBuilder = Chat.newBuilder();
    chatBuilder.setId(null);
    chatBuilder.setTitle(req.getTitle());
    for (String userId : req.getParticipantsList()) {
      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(userId);
      userBuilder.setUsername("mock_username");
      chatBuilder.addParticipants(userBuilder);
    }

    CreateChatResponse chatResponse = CreateChatResponse.newBuilder().setChat(chatBuilder).build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void updateChat(UpdateChatRequest req, StreamObserver<UpdateChatResponse> responseObserver) {
    Chat.Builder chatBuilder = Chat.newBuilder();
    chatBuilder.setId(req.getId());
    chatBuilder.setTitle(req.getTitle());
    for (String userId : req.getParticipantsList()) {
      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(userId);
      userBuilder.setUsername("mock_username");
      chatBuilder.addParticipants(userBuilder);
    }

    UpdateChatResponse chatResponse = UpdateChatResponse.newBuilder().setChat(chatBuilder).build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getMessages(GetMessagesRequest req, StreamObserver<GetMessagesResponse> responseObserver) {
    GetMessagesResponse.Builder messageListResponseBuilder = GetMessagesResponse.newBuilder();

    String chatId = req.getChatId();

    messageListResponseBuilder.addMessages(buildMockMessage());
    messageListResponseBuilder.addMessages(buildMockMessage());
    messageListResponseBuilder.addMessages(buildMockMessage());
    messageListResponseBuilder.addMessages(buildMockMessage());

    responseObserver.onNext(messageListResponseBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void sendMessage(SendMessageRequest req, StreamObserver<SendMessageResponse> responseObserver) {
    Random random = new Random();

    Message.Builder messageBuilder = Message.newBuilder();
    messageBuilder.setId(null);
    messageBuilder.setMessage(req.getMessage());
    messageBuilder.setSender("id");

    SendMessageResponse messageResponse = SendMessageResponse.newBuilder().setMessage(messageBuilder).build();

    responseObserver.onNext(messageResponse);
    responseObserver.onCompleted();
  }
}
