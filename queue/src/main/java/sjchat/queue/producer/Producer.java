package sjchat.queue.producer;

import sjchat.queue.QueueChannel;
import sjchat.queue.QueueException;
import sjchat.queue.workitem.WorkItem;

public abstract class Producer {

  private QueueChannel queue;

  public Producer(QueueChannel queue) {
    this.queue = queue;
  }

  public void dispatchWorkItem(WorkItem workItem) throws QueueException {
    queue.dispatchMessage(workItem.getRawData());
  }

}
