package sjchat.users;

import java.io.IOException;
import java.util.Random;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import sjchat.users.UserAuthentication;

public class UserServiceServer {
  private Server server;
  private final int port = 50051;

  public static void main(String[] args) throws Exception {
    UserServiceServer userServiceServer = new UserServiceServer();
    userServiceServer.start();
    userServiceServer.blockUntilShutdown();
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

  static class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void createUser(CreateUserRequest req, StreamObserver<CreateUserResponse> responseObserver) {
      Random random = new Random();

      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(Math.abs(random.nextInt(100)));
      userBuilder.setUsername(req.getUsername());

      CreateUserResponse userResponse = CreateUserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void getUser(GetUserRequest req, StreamObserver<GetUserResponse> responseObserver) {
      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(req.getId());
      userBuilder.setUsername("user_" + req.getId());

      GetUserResponse userResponse = GetUserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest req, StreamObserver<UpdateUserResponse> responseObserver) {
      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(req.getId());
      userBuilder.setUsername(req.getUsername());

      UpdateUserResponse userResponse = UpdateUserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void loginUser(LoginRequest req, StreamObserver<LoginResponse> responseObserver) {
      LoginResponse loginResponse = LoginResponse.newBuilder().setAuthenticated(true).build();
      responseObserver.onNext(loginResponse);
      responseObserver.onCompleted();
    }

    /**
     * Logout from all devices
     */
    public void logout(LogoutRequest req, StreamObserver<LogoutResponse> responseObserver) {
      LogoutResponse logoutResponse = LogoutResponse
        .newBuilder().setAuthenticated(true).build();
      responseObserver.onCompleted();
    }

  }
}
