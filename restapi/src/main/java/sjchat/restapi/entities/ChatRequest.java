package sjchat.restapi.entities;

public class ChatRequest {

  private String title;
  private String[] participants;

  public String getTitle() {
    return title;
  }

  public String[] getParticipants() {
    return participants;
  }
}