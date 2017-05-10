package sjchat.queue;

public class MessageExchange extends Exchange {
  final static String EXCHANGE_NAME = "message_exchange";

  protected String getExchangeName() {
    return EXCHANGE_NAME;
  }
}
