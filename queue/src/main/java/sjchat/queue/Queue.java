package sjchat.queue;

import java.io.IOException;

import sjchat.queue.consumer.Consumer;

public abstract class Queue extends QueueChannel {

  abstract String getQueueName();

  public void initialize() throws QueueException {
    super.initialize();

    try {
      getChannel().queueDeclare(getQueueName(), true, false, false, null);
    } catch (IOException exception) {
      shutdown();
      throw QueueException.initializationFailed(this, exception);
    }
  }

  public void bindQueue(String exchangeName) throws QueueException {
    try {
      getChannel().queueBind(getQueueName(), exchangeName, "");
    } catch (IOException exception) {
      shutdown();
      throw QueueException.initializationFailed(this, exception);
    }
  }

  public void dispatchMessage(String message) throws QueueException {
    try {
      getChannel().basicPublish("", getQueueName(), null, message.getBytes());
    } catch (IOException exception) {
      shutdown();
      throw QueueException.dispatchFailed(message, this, exception);
    }
  }

  public void addConsumer(Consumer consumer, boolean autoAck) throws QueueException {
    try {
      getChannel().basicConsume(getQueueName(), autoAck, consumer);
    } catch (IOException exception) {
      shutdown();
      throw QueueException.failedAddingConsumer(this, exception);
    }
  }

  public String toString() {
    return getQueueName();
  }
}
