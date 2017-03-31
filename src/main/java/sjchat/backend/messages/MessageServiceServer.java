package sjchat.backend.messages;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class MessageServiceServer {
  private Server server;
  private int port = 50052;

  public static void main(String[] args) throws Exception {
    MessageServiceServer messageServiceServer = new MessageServiceServer();
    messageServiceServer.start();
    messageServiceServer.blockUntilShutdown();
  }

  public void start() throws IOException {
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

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  static class MessageService extends MessageServiceGrpc.MessageServiceImplBase {

    @Override
    public void getChat(ChatRequest req, StreamObserver<ChatResponse> responseObserver) {
      Chat.Builder chatBuilder = Chat.newBuilder();
      chatBuilder.setId(req.getId());
      chatBuilder.setTitle("Test1323");

      Chat chat = chatBuilder.build();

      ChatResponse chatResponse = ChatResponse.newBuilder().setChat(chat).build();

      responseObserver.onNext(chatResponse);
      responseObserver.onCompleted();
    }
  }
}
