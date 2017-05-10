package sjchat.queue.producer;

import sjchat.messages.Message;
import sjchat.queue.Queue;
import sjchat.queue.QueueChannel;
import sjchat.queue.QueueException;
import sjchat.queue.workitem.MessageWorkItem;

public class MessageProducer extends Producer {

  public MessageProducer(QueueChannel queue) {
    super(queue);
  }

  public void dispatchMessage(Message message) throws QueueException {
    MessageWorkItem workItem = new MessageWorkItem();
    workItem.setMessage(message);
    dispatchWorkItem(workItem);
  }
}
