package sjchat.queue.workitem;

import com.rabbitmq.client.Envelope;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class WorkItem {
  private String id;
  private JSONObject data;
  private Envelope envelope;

  public WorkItem() {
    this.data = new JSONObject();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public JSONObject getDataObject() {
    return data;
  }

  public String getRawData() {
    return data.toJSONString();
  }

  public void setRawData(String rawData) {
    JSONParser parser = new JSONParser();
    try {
      data = (JSONObject) parser.parse(rawData);
    } catch (ParseException exception) {
      System.out.println("Could not parse data for work item from envelope" + getEnvelope().toString());
    }
  }

  public Envelope getEnvelope() {
    return envelope;
  }

  public void setEnvelope(Envelope envelope) {
    this.envelope = envelope;
  }

  protected Object getDataValue(String key, Class expectedClass) {
    if (getDataObject().containsKey(key)) {
      Object value = getDataObject().get(key);
      if (expectedClass.isInstance(value)) {
        return value;
      }
    }
    return null;
  }
}
