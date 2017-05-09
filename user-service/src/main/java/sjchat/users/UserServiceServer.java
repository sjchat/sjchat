package sjchat.users;

import java.io.IOException;
import java.util.Random;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import sjchat.users.UserAuthentication;
import sjchat.users.tokens.AuthenticationResult;
import sjchat.users.UserService;

public class UserServiceServer {
  private Server server;
  private final int port;

  public static void main(String[] args) throws Exception {
    UserServiceServer userServiceServer = new UserServiceServer(UserServerConfig.getPort());
    userServiceServer.start();
    userServiceServer.blockUntilShutdown();
  }

  public UserServiceServer(int port) {
    this.port = port;
  }

  private void start() throws IOException {
    server = ServerBuilder.forPort(port).addService(new UserService()).build().start();
    System.out.println("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        UserServiceServer.this.stop();
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
}
