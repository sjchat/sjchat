package sjchat.queue.consumer;

import com.rabbitmq.client.Envelope;

import sjchat.messages.Message;
import sjchat.queue.QueueChannel;
import sjchat.queue.workitem.MessageWorkItem;

public class MessageConsumer extends Consumer {
  private MessageConsumerCallback callback;

  public MessageConsumer(QueueChannel queue) {
    super(queue);
  }

  public void setCallback(MessageConsumerCallback callback) {
    this.callback = callback;
  }

  public void processDelivery(String rawData, Envelope envelope) {

    final MessageWorkItem workItem = new MessageWorkItem();
    workItem.setEnvelope(envelope);
    workItem.setRawData(rawData);

    Message message = workItem.getMessage();

    if (callback != null) {
      callback.consumeMessage(message);
    }

    workItemFinished(workItem);
  }
}
