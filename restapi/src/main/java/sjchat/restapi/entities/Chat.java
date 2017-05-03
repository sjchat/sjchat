package sjchat.restapi.entities;

import java.util.ArrayList;
import java.util.List;

public class Chat {

  private String id;
  private String title;
  private List<User> participants;

  public Chat() {
    participants = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<User> getParticipants() {
    return participants;
  }

  public void addParticipant(User user) {
    participants.add(user);
  }
}