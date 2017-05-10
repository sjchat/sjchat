package sjchat.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import sjchat.queue.consumer.Consumer;

public abstract class QueueChannel {

  private final static String QUEUE_HOST = "localhost";
  private Connection connection;
  private Channel channel;

  public abstract String toString();

  public Channel getChannel() {
    return channel;
  }

  public void initialize() throws QueueException {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(QUEUE_HOST);

      connection = factory.newConnection();
      channel = connection.createChannel();

    } catch (IOException exception) {
      shutdown();
      throw QueueException.initializationFailed(this, exception);
    } catch (TimeoutException exception) {
      shutdown();
      throw QueueException.initializationFailed(this, exception);
    }
  }

  protected void shutdown() throws QueueException {
    try {
      channel.close();
      connection.close();
    } catch (IOException exception) {
      shutdown();
      throw QueueException.shutdownFailed(this, exception);
    } catch (TimeoutException exception) {
      shutdown();
      throw QueueException.shutdownFailed(this, exception);
    }
  }

  public abstract void dispatchMessage(String message) throws QueueException;
}
