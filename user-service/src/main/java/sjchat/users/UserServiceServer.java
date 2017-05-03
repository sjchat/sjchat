package sjchat.users;

import sjchat.general.GRPCServer;

public class UserServiceServer {
  private static final int port = 50051;

  public static void main(String[] args) throws Exception {
    GRPCServer messageServiceServer = new GRPCServer(new UserService(), port);
    messageServiceServer.start();
    UserServiceServer userServiceServer = new UserServiceServer();
    userServiceServer.start();
    userServiceServer.blockUntilShutdown();
  }

  private void start() throws IOException {
    ;
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
    public void createUser(UserDataRequest req, StreamObserver<UserResponse> responseObserver) {
      Random random = new Random();

      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(Math.abs(random.nextInt(100)));
      userBuilder.setUsername(req.getUsername());

      UserResponse userResponse = UserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void getUser(UserRequest req, StreamObserver<UserResponse> responseObserver) {
      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(req.getId());
      userBuilder.setUsername("user_" + req.getId());

      UserResponse userResponse = UserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UserDataRequest req, StreamObserver<UserResponse> responseObserver) {
      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(req.getId());
      userBuilder.setUsername(req.getUsername());

      UserResponse userResponse = UserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    public void loginUser(LoginRequest req, StreamObserver<AuthResponse> responseObserver) {
      
    }

    /**
     * Logout from all devices
     */
    public void logout(LogoutRequest req, StreamObserver)

>>>>>>> start of authentication functions
  }
}
