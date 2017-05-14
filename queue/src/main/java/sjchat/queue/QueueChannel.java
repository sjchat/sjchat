package sjchat.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class QueueChannel {

  private Connection connection;
  private Channel channel;

  public abstract String toString();

  public Channel getChannel() {
    return channel;
  }

  public void initialize() throws QueueException {
    String host = System.getenv("QUEUE_HOST");
    host = (host == null) ? "localhost" : host;

    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(host);

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
      if (channel != null) {
        channel.close();
      }
      if (connection != null) {
        connection.close();
      }
    } catch (IOException exception) {
      throw QueueException.shutdownFailed(this, exception);
    } catch (TimeoutException exception) {
      throw QueueException.shutdownFailed(this, exception);
    }
  }

  public abstract void dispatchMessage(String message) throws QueueException;
}
