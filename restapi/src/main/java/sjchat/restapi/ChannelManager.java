package sjchat.restapi;

public class ChannelManager {
  private static ChannelManager instance = null;
  private Channel messageServiceChannel;
  private Channel userServiceChannel;

  private ChannelManager() {
    buildMessageServiceChannel();
    buildUserServiceChannel();
  }

  public static ChannelManager getInstance() {
    if(instance == null) {
      instance = new ChannelManager();
    }
    return instance;
  }

  private void buildMessageServiceChannel() {
    this.messageServiceChannel = ManagedChannelBuilder
      .forAddress("localhost", 50052) //TODO: Put port in config file
      .usePlaintext(true)
      .build();
  }

  private void buildUserServiceChannel() {
    this.userServiceChannel = ManagedChannelBuilder
      .forAddress("localhost", 50051) //TODO: Put port in config file
      .usePlaintext(true)
      .build();
  }

  public Channel getMessageServiceChannel() {
    return messageServiceChannel;
  }

  public Channel getUserServiceChannel() {
    return userServiceChannel;
  }
}
