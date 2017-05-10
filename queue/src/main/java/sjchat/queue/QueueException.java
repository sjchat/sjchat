package sjchat.queue;

public class QueueException extends Exception {

  public QueueException(String message) {
    super(message);
  }

  public QueueException(String message, Throwable cause) {
    super(message, cause);
  }

  public static QueueException initializationFailed(QueueChannel queueChannel) {
    String message = "Could not initialize queue " + queueChannel.toString();
    return new QueueException(message);
  }

  public static QueueException initializationFailed(QueueChannel queueChannel, Throwable cause) {
    String message = "Could not initialize queue " + queueChannel.toString();
    return new QueueException(message, cause);
  }

  public static QueueException shutdownFailed(QueueChannel queueChannel) {
    String message = "Could not shutdown queue " + queueChannel.toString();
    return new QueueException(message);
  }

  public static QueueException shutdownFailed(QueueChannel queueChannel, Throwable cause) {
    String message = "Could not shutdown queue " + queueChannel.toString();
    return new QueueException(message, cause);
  }

  public static QueueException dispatchFailed(String queueMessage, QueueChannel queueChannel) {
    String message = "Could not dispatch message \"" + queueMessage + "\" in queue queue " + queueChannel.toString();
    return new QueueException(message);
  }

  public static QueueException dispatchFailed(String queueMessage, QueueChannel queueChannel, Throwable cause) {
    String message = "Could not dispatch message \"" + queueMessage + "\" in queue queue " + queueChannel.toString();
    return new QueueException(message, cause);
  }

  public static QueueException failedAddingConsumer(QueueChannel queueChannel) {
    String message = "Could not add consumer queue queue " + queueChannel.toString();
    return new QueueException(message);
  }

  public static QueueException failedAddingConsumer(QueueChannel queueChannel, Throwable cause) {
    String message = "Could not add consumer queue queue " + queueChannel.toString();
    return new QueueException(message, cause);
  }
}
