package sjchat.restapi.websocket.models;

public class SendMessageAction extends AbstractAction {
  private long chatId;
  private String message;

  public String getAction() {
    return "sendMessage";
  }

  public long getChatId() {
    return chatId;
  }

  public String getMessage () {
    return message;
  }

}
