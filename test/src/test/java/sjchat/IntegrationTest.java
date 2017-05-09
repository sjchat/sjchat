package sjchat;

import java.io.IOException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public final class IntegrationTest {
  
  public IntegrationTest() {}
  
  @BeforeClass
  public static void setUpClass() {
    DockerSetup.startDocker();
    
    // make sure all services are properly started
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}
  }
  
  @AfterClass
  public static void tearDownClass() {
    DockerSetup.stopDocker();
  }
  
  @Test
  public void testGetChatList() throws IOException {
    HttpResponse response = HttpUtil.httpGet("http://192.168.99.100:8080/chat");
    
    System.out.println(response);
    
    assertEquals(200, response.responseCode);
  }
  
  @Test
  public void testLoadClientPage() throws IOException {
    HttpResponse response = HttpUtil.httpGet("http://192.168.99.100:8082/client.html");
    
    System.out.println(response);
    
    assertEquals(200, response.responseCode);
  }
}
