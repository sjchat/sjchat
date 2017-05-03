package sjchat.restapi.entities;

public class Message {

  private String id;
  private String message;
  private String user;

  public Message() {
  }

  public Message(String id, String message, String user) {
    this.id = id;
    this.message = message;
    this.user = user;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }
}
