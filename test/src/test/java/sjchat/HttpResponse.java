package sjchat;

public class HttpResponse {
  
  public final int responseCode;
  public final String data;
  
  public HttpResponse(int responseCode, String data) {
    this.responseCode = responseCode;
    this.data = data;
  }
  
  @Override
  public String toString() {
    return "---" + responseCode + "---\n" + data;
  }
}
