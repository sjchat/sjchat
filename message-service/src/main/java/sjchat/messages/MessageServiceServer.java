package sjchat.messages;

import sjchat.general.GRPCServer;

public class MessageServiceServer {
  private static final int port = 50052;

  public static void main(String[] args) throws Exception {
    GRPCServer messageServiceServer = new GRPCServer(new MessageService(), port);
    messageServiceServer.start();
  }
}
