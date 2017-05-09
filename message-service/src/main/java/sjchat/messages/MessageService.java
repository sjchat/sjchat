package sjchat.messages;

import java.util.Random;

import io.grpc.stub.StreamObserver;
import sjchat.users.User;

class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
  private static Chat.Builder buildMockChat(String id) {
    Random random = new Random();
    Chat.Builder chatBuilder = Chat.newBuilder();
    chatBuilder.setId(id);
    chatBuilder.setTitle("Test chat " + chatBuilder.getId());
    return chatBuilder;
  }

  private static User.Builder buildMockUser(String id) {
    Random random = new Random();
    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId(id);
    userBuilder.setUsername("user_" + userBuilder.getId());
    return userBuilder;
  }

  private static Message.Builder buildMockMessage(String id) {
    Random random = new Random();
    Message.Builder messageBuilder = Message.newBuilder();
    messageBuilder.setId(id);
    messageBuilder.setMessage("Test message " + messageBuilder.getId());
    messageBuilder.setSender("user-" + id);
    return messageBuilder;
  }

  @Override
  public void getChatList(GetChatListRequest req, StreamObserver<GetChatListResponse> responseObserver) {
    GetChatListResponse.Builder chatListResponseBuilder = GetChatListResponse.newBuilder();

    User.Builder userBuilder1 = buildMockUser("user-1");
    User.Builder userBuilder2 = buildMockUser("user-2");

    Chat.Builder chatBuilder1 = buildMockChat("chat-1");
    chatBuilder1.addParticipants(userBuilder1);
    chatBuilder1.addParticipants(userBuilder2);
    chatListResponseBuilder.addChats(chatBuilder1);

    Chat.Builder chatBuilder2 = buildMockChat("chat-2");
    chatBuilder2.addParticipants(userBuilder1);
    chatBuilder2.addParticipants(userBuilder2);
    chatListResponseBuilder.addChats(chatBuilder2);

    GetChatListResponse chatResponse = chatListResponseBuilder.build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getChat(GetChatRequest req, StreamObserver<GetChatResponse> responseObserver) {
    User.Builder userBuilder1 = buildMockUser("user-1");
    User.Builder userBuilder2 = buildMockUser("user-2");

    Chat.Builder chatBuilder1 = buildMockChat("chat-1");
    chatBuilder1.addParticipants(userBuilder1);
    chatBuilder1.addParticipants(userBuilder2);

    GetChatResponse chatResponse = GetChatResponse.newBuilder().setChat(chatBuilder1).build();

    responseObserver.onNext(chatResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void createChat(CreateChatRequest req, StreamObserver<CreateChatResponse> responseObserver) {
    Random random = new Random();

    Chat.Builder chatBuilder = Chat.newBuilder();
    chatBuilder.setId("mock");
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

    messageListResponseBuilder.addMessages(buildMockMessage("msg-1"));
    messageListResponseBuilder.addMessages(buildMockMessage("msg-2"));
    messageListResponseBuilder.addMessages(buildMockMessage("msg-3"));
    messageListResponseBuilder.addMessages(buildMockMessage("msg-4"));

    responseObserver.onNext(messageListResponseBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void sendMessage(SendMessageRequest req, StreamObserver<SendMessageResponse> responseObserver) {
    Random random = new Random();

    Message.Builder messageBuilder = Message.newBuilder();
    messageBuilder.setId("msg-1");
    messageBuilder.setMessage(req.getMessage());
    messageBuilder.setSender("user-id");

    SendMessageResponse messageResponse = SendMessageResponse.newBuilder().setMessage(messageBuilder).build();

    responseObserver.onNext(messageResponse);
    responseObserver.onCompleted();
  }
}
