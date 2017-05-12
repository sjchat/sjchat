package sjchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HttpUtil {
  
  private HttpUtil() {}
  
  public static HttpResponse httpGet(String url, int timeout) throws IOException {
    return httpGet(new URL(url), timeout);
  }
  
  public static HttpResponse httpGet(URL url, int timeout) throws IOException {
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    
    con.setRequestMethod("GET");
    con.setConnectTimeout(timeout);
    
    StringBuilder response = new StringBuilder();
    
    try (BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
      while (true) {
        String line = r.readLine();
        if (line == null) break;
        response.append(line).append('\n');
      }
    }
    
    return new HttpResponse(con.getResponseCode(), response.toString());
  }
}
