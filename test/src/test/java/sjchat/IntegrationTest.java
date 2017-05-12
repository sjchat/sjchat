package sjchat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public final class IntegrationTest {
  
  private static final String IP_ADDR = "192.168.99.100";
  
  public IntegrationTest() {}
  
  @BeforeClass
  public static void setUpClass() {
    DockerSetup.startDocker();
    
    // make sure all services are properly started
    try {
      Thread.sleep(10000);
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
      sock.connect(new InetSocketAddress(addr, port), 10000);
      
      Assert.isTrue(sock.isConnected(), "Unable to connect to " + addr + ":" + port);
    } catch (IOException e) {
      Assert.fail("Unable to connect to " + addr + ":" + port + " (" + e.toString() + ")");
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
    HttpResponse response = HttpUtil.httpGet("http://" + IP_ADDR + ":8080/chat");
    assertEquals(200, response.responseCode);
    
    System.out.println(response);
    
    JsonElement json = new JsonParser().parse(response.data);
    
    // chat list should be an empty array
    assertTrue(json.isJsonArray());
    assertEquals(0, ((JsonArray)json).size());
  }
  
  @Test
  public void testMockGetChatResponse() throws IOException {
    HttpResponse response = HttpUtil.httpGet("http://" + IP_ADDR + ":8080/chat/1/");
    assertEquals(200, response.responseCode);
    
    System.out.println(response);
  }
  
  @Test
  public void testLoadClientPage() throws IOException {
    HttpResponse response = HttpUtil.httpGet("http://192.168.99.100:8082/client.html");
    assertEquals(200, response.responseCode);
    
    System.out.println(response);
  }
}
