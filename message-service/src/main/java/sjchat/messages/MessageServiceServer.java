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
    public void getChatList(ChatListRequest req, StreamObserver<ChatListResponse> responseObserver) {
      ChatListResponse.Builder chatListResponseBuilder = ChatListResponse.newBuilder();

      User.Builder userBuilder1 = buildMockUser();
      User.Builder userBuilder2 = buildMockUser();

      Chat.Builder chatBuilder1 = buildMockChat();
      chatBuilder1.addUsers(userBuilder1);
      chatBuilder1.addUsers(userBuilder2);
      chatListResponseBuilder.addChats(chatBuilder1);

      Chat.Builder chatBuilder2 = buildMockChat();
      chatBuilder2.addUsers(userBuilder1);
      chatBuilder2.addUsers(userBuilder2);
      chatListResponseBuilder.addChats(chatBuilder2);

      ChatListResponse chatResponse = chatListResponseBuilder.build();

      responseObserver.onNext(chatResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void getChat(ChatRequest req, StreamObserver<ChatResponse> responseObserver) {
      User.Builder userBuilder1 = buildMockUser();
      User.Builder userBuilder2 = buildMockUser();

      Chat.Builder chatBuilder1 = buildMockChat();
      chatBuilder1.addUsers(userBuilder1);
      chatBuilder1.addUsers(userBuilder2);

      ChatResponse chatResponse = ChatResponse.newBuilder().setChat(chatBuilder1).build();

      responseObserver.onNext(chatResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void createChat(ChatDataRequest req, StreamObserver<ChatResponse> responseObserver) {
      Random random = new Random();

      Chat.Builder chatBuilder = Chat.newBuilder();
      chatBuilder.setId(Math.abs(random.nextInt(100)));
      chatBuilder.setTitle(req.getTitle());
      for (long userId : req.getUsersList()) {
        User.Builder userBuilder = User.newBuilder();
        userBuilder.setId(userId);
        userBuilder.setUsername("mock_username");
        chatBuilder.addUsers(userBuilder);
      }

      ChatResponse chatResponse = ChatResponse.newBuilder().setChat(chatBuilder).build();

      responseObserver.onNext(chatResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void updateChat(ChatDataRequest req, StreamObserver<ChatResponse> responseObserver) {
      Chat.Builder chatBuilder = Chat.newBuilder();
      chatBuilder.setId(req.getId());
      chatBuilder.setTitle(req.getTitle());
      for (long userId : req.getUsersList()) {
        User.Builder userBuilder = User.newBuilder();
        userBuilder.setId(userId);
        userBuilder.setUsername("mock_username");
        chatBuilder.addUsers(userBuilder);
      }

      ChatResponse chatResponse = ChatResponse.newBuilder().setChat(chatBuilder).build();

      responseObserver.onNext(chatResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void getMessages(MessageListRequest req, StreamObserver<MessageListResponse> responseObserver) {
      MessageListResponse.Builder messageListResponseBuilder = MessageListResponse.newBuilder();

      long chatId = req.getChatId();

      messageListResponseBuilder.addMessages(buildMockMessage());
      messageListResponseBuilder.addMessages(buildMockMessage());
      messageListResponseBuilder.addMessages(buildMockMessage());
      messageListResponseBuilder.addMessages(buildMockMessage());

      responseObserver.onNext(messageListResponseBuilder.build());
      responseObserver.onCompleted();
    }

    @Override
    public void sendMessage(NewMessageRequest req, StreamObserver<MessageResponse> responseObserver) {
      Random random = new Random();

      Message.Builder messageBuilder = Message.newBuilder();
      messageBuilder.setId(Math.abs(random.nextInt(100)));
      messageBuilder.setMessage(req.getMessage());
      messageBuilder.setUser(123);

      MessageResponse messageResponse = MessageResponse.newBuilder().setMessage(messageBuilder).build();

      responseObserver.onNext(messageResponse);
      responseObserver.onCompleted();
    }
  }
}
