package sjchat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public final class IntegrationTest {
  
  private static String IP_ADDR = null;
  private static int TIMEOUT = -1;
  private static int STARTUP_WAIT = -1;
  
  public IntegrationTest() {}
  
  @BeforeClass
  public static void setUpClass() {
    DockerSetup.startDocker();
    
    IP_ADDR = System.getProperty("dockerIP");
    if (IP_ADDR == null && !IP_ADDR.isEmpty()) {
      System.out.println("Property \"dockerIP\" not found, defaulting to localhost.");
      IP_ADDR = "localhost";
    }
    
    try {
      String p = System.getProperty("timeout");
      if (p != null && !p.isEmpty()) {
        TIMEOUT = Math.max(Integer.parseInt(System.getProperty("timeout")), 0);
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid integer: " + System.getProperty("timeout"));
    }
    
    if (TIMEOUT < 0) {
      System.out.println("Property \"timeout\" not found, defaulting to 10000 ms.");
      TIMEOUT = 10000;
    }
    
    try {
      String p = System.getProperty("startupWait");
      if (p != null && !p.isEmpty()) {
        STARTUP_WAIT = Math.max(Integer.parseInt(System.getProperty("startupWait")), 0);
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid integer: " + System.getProperty("startupWait"));
    }
    
    if (STARTUP_WAIT < 0) {
      System.out.println("Property \"startupWait\" not found, defaulting to 10000 ms.");
      STARTUP_WAIT = 10000;
    }
    
    // make sure all services are properly started
    try {
      Thread.sleep(STARTUP_WAIT);
    } catch (InterruptedException e) {}
  }
  
  @AfterClass
  public static void tearDownClass() {
    DockerSetup.stopDocker();
  }
  
  /**
   * Attempts to connect to the specified address, and calls Assert.fail() if that fails.
   */
  private void testConnect(String addr, int port) {
    try (Socket sock = new Socket()) {
      sock.connect(new InetSocketAddress(addr, port), TIMEOUT);
      
      assertTrue(sock.isConnected());
    } catch (IOException e) {
      fail("Unable to connect to " + addr + ":" + port + " (" + e.toString() + ")");
    }
  }
  
  @Test
  public void testConnectToRestApi() throws IOException {
    testConnect(IP_ADDR, 8080);
  }
  
  @Test
  public void testConnectToMessageService() throws IOException {
    testConnect(IP_ADDR, 50052);
  }
  
  @Test
  public void testConnectToDatabase() throws IOException {
    testConnect(IP_ADDR, 27017);
  }
  
  @Test
  public void testConnectToUserService() throws IOException {
    testConnect(IP_ADDR, 50051);
  }
  
  @Test
  public void testConnectToWebClient() throws IOException {
    testConnect(IP_ADDR, 8082);
  }
  
  @Test
  public void testGetEmptyChatList() throws IOException {
    HttpResponse response = HttpUtil.httpGet("http://" + IP_ADDR + ":8080/chat", TIMEOUT);
    assertEquals(200, response.responseCode);
    
    System.out.println(response);
    
    JsonElement json = new JsonParser().parse(response.data);
    
    // chat list should be an empty array
    assertTrue(json.isJsonArray());
    assertEquals(0, ((JsonArray)json).size());
  }
  
  @Test
  public void testMockGetChatResponse() throws IOException {
    HttpResponse response = HttpUtil.httpGet("http://" + IP_ADDR + ":8080/chat/1/", TIMEOUT);
    assertEquals(200, response.responseCode);
    
    System.out.println(response);
  }
  
  @Test
  public void testLoadClientPage() throws IOException {
    HttpResponse response = HttpUtil.httpGet("http://" + IP_ADDR + ":8082/client.html", TIMEOUT);
    assertEquals(200, response.responseCode);
    
    System.out.println(response);
  }
}
