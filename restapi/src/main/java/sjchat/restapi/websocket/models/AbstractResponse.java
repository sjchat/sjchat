package sjchat.restapi.websocket.models;

public abstract class AbstractResponse {
  private String status;

  public String getStatus() {
    return status;
  }

  protected void setStatus(String status) {
    this.status = status;
  }

}
