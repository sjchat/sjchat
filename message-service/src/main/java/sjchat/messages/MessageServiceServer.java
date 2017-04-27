package sjchat.messages;

import java.io.IOException;
import java.util.Random;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import sjchat.users.User;

public class MessageServiceServer {
  private Server server;
  private int port = 50052;

  public static void main(String[] args) throws Exception {
    MessageServiceServer messageServiceServer = new MessageServiceServer();
    messageServiceServer.start();
    messageServiceServer.blockUntilShutdown();
  }

  private void start() throws IOException {
    ;
    server = ServerBuilder.forPort(port).addService(new MessageService()).build().start();
    System.out.println("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        MessageServiceServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  private static Chat.Builder buildMockChat() {
    Random random = new Random();
    Chat.Builder chatBuilder = Chat.newBuilder();
    chatBuilder.setId(Math.abs(random.nextInt(100)));
    chatBuilder.setTitle("Test chat " + chatBuilder.getId());
    return chatBuilder;
  }

  private static User.Builder buildMockUser() {
    Random random = new Random();
    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId(Math.abs(random.nextInt(100)));
    userBuilder.setUsername("user_" + userBuilder.getId());
    return userBuilder;
  }

  private static Message.Builder buildMockMessage() {
    Random random = new Random();
    Message.Builder messageBuilder = Message.newBuilder();
    messageBuilder.setId(Math.abs(random.nextInt(100)));
    messageBuilder.setMessage("Test message " + messageBuilder.getId());
    messageBuilder.setUser(123);
    return messageBuilder;
  }

  static class MessageService extends MessageServiceGrpc.MessageServiceImplBase {

    @Override
    public void getChatList(GetChatListRequest req, StreamObserver<GetChatListResponse> responseObserver) {
      GetChatListResponse.Builder chatListResponseBuilder = GetChatListResponse.newBuilder();

      User.Builder userBuilder1 = buildMockUser();
      User.Builder userBuilder2 = buildMockUser();

      Chat.Builder chatBuilder1 = buildMockChat();
      chatBuilder1.addParticipants(userBuilder1);
      chatBuilder1.addParticipants(userBuilder2);
      chatListResponseBuilder.addChats(chatBuilder1);

      Chat.Builder chatBuilder2 = buildMockChat();
      chatBuilder2.addParticipants(userBuilder1);
      chatBuilder2.addParticipants(userBuilder2);
      chatListResponseBuilder.addChats(chatBuilder2);

      GetChatListResponse chatResponse = chatListResponseBuilder.build();

      responseObserver.onNext(chatResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void getChat(GetChatRequest req, StreamObserver<GetChatResponse> responseObserver) {
      User.Builder userBuilder1 = buildMockUser();
      User.Builder userBuilder2 = buildMockUser();

      Chat.Builder chatBuilder1 = buildMockChat();
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
      chatBuilder.setId(Math.abs(random.nextInt(100)));
      chatBuilder.setTitle(req.getTitle());
      for (long userId : req.getParticipantsList()) {
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
      for (long userId : req.getParticipantsList()) {
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

      long chatId = req.getChatId();

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
      messageBuilder.setId(Math.abs(random.nextInt(100)));
      messageBuilder.setMessage(req.getMessage());
      messageBuilder.setUser(123);

      SendMessageResponse messageResponse = SendMessageResponse.newBuilder().setMessage(messageBuilder).build();

      responseObserver.onNext(messageResponse);
      responseObserver.onCompleted();
    }
  }
}
