package sjchat.users;

import sjchat.general.GRPCServer;

public class UserServiceServer {
  private static final int port = 50051;

  public static void main(String[] args) throws Exception {
    GRPCServer userServiceServer = new GRPCServer(new UserService(), port);
    userServiceServer.start();
    userServiceServer.blockUntilShutdown();
  }
}
