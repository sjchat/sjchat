package sjchat.restapi.entities;

public class ChatRequest {

  private String title;
  private long[] users;

  public String getTitle() {
    return title;
  }

  public long[] getUsers() {
    return users;
  }
}