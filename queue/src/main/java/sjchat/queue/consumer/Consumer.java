package sjchat.queue.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

import sjchat.queue.QueueChannel;
import sjchat.queue.workitem.WorkItem;

public abstract class Consumer extends com.rabbitmq.client.DefaultConsumer {

  private QueueChannel queue;

  public Consumer(QueueChannel queue) {
    super(queue.getChannel());
    this.queue = queue;
  }

  abstract void processDelivery(String rawData, Envelope envelope);

  @Override
  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
    String rawData = new String(body, "UTF-8");
    this.processDelivery(rawData, envelope);
  }

  protected void workItemFinished(WorkItem workItem) {
    try {
      queue.getChannel().basicAck(workItem.getEnvelope().getDeliveryTag(), false);
    } catch (IOException exception) {
      System.out.println("Could not ack work item with envelope " + workItem.getEnvelope().toString());
    }
  }
}
