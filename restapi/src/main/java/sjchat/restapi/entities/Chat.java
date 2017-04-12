package sjchat.restapi.entities;

import java.util.ArrayList;
import java.util.List;

public class Chat {

  private long id;
  private String title;
  private List<User> users;

  public Chat() {
    users = new ArrayList<>();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<User> getUsers() {
    return users;
  }

  public void addUser(User user) {
    users.add(user);
  }
}