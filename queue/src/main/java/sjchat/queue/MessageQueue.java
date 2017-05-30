package sjchat.queue;


import sjchat.queue.consumer.Consumer;

public class MessageQueue extends Queue {
  private final static String QUEUE_NAME = "message_queue";

  protected String getQueueName() {
    return QUEUE_NAME;
  }

  public void initialize() throws QueueException {
    super.initialize();
    bindQueue(MessageExchange.EXCHANGE_NAME);
  }

  public void addConsumer(Consumer consumer) throws QueueException {
    super.addConsumer(consumer, false);
  }
}
