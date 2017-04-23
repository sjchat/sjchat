package sjchat.restapi.entities;

public class ChatRequest {

  private String title;
  private String[] users;

  public String getTitle() {
    return title;
  }

  public String[] getUsers() {
    return users;
  }
}