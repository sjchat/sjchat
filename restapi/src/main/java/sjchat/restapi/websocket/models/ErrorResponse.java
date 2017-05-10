package sjchat.restapi.websocket.models;

public class ErrorResponse extends AbstractResponse {
  private String message;

  public ErrorResponse(String message) {
    setStatus("error");
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
