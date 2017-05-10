package sjchat.queue.consumer;

import sjchat.messages.Message;

public interface MessageConsumerCallback {
  public void consumeMessage(Message message);
}
