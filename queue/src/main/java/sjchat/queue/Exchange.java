package sjchat.queue;


import java.io.IOException;


public abstract class Exchange extends QueueChannel {

  abstract String getExchangeName();

  public void initialize() throws QueueException {
    super.initialize();

    try {
      getChannel().exchangeDeclare(getExchangeName(), "fanout");
    } catch (IOException exception) {
      shutdown();
      throw QueueException.initializationFailed(this, exception);
    }
  }

  public void dispatchMessage(String message) throws QueueException {
    try {
      getChannel().basicPublish(getExchangeName(), "", null, message.getBytes());
    } catch (IOException exception) {
      shutdown();
      throw QueueException.dispatchFailed(message, this, exception);
    }
  }

  public String toString() {
    return getExchangeName();
  }
}
