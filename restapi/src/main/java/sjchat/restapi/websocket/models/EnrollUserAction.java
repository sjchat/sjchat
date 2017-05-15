package sjchat.restapi.websocket.models;

public class EnrollUserAction extends AbstractAction {
  private String userId;

  public String getAction() {
    return "enrollUser";
  }

  public String getUserId() {
    return userId;
  }
}
