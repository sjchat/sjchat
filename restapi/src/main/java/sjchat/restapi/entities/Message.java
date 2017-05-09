package sjchat.restapi.entities;

public class Message {

  private String id;
  private String message;
  private String sender;

  public Message() {
  }

  public Message(String id, String message, String sender) {
    this.id = id;
    this.message = message;
    this.sender = sender;
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

  public String getSender() {
    return sender;
  }

  public void setUser(String user) {
    this.sender = user;
  }
}
