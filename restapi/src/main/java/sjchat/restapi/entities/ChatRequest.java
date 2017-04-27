package sjchat.restapi.entities;

public class ChatRequest {

  private String title;
  private long[] participants;

  public String getTitle() {
    return title;
  }

  public long[] getParticipants() {
    return participants;
  }
}