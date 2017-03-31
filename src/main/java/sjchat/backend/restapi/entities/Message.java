package sjchat.backend.restapi.entities;

public class Message {
  private long id;
  private String message;
  private long user;

  public Message() {
  }

  public Message(long id, String message, long user) {
    this.id = id;
    this.message = message;
    this.user = user;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public long getUser() {
    return user;
  }
}
