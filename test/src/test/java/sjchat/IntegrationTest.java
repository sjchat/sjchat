package sjchat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public final class IntegrationTest {
  
  public IntegrationTest() {}
  
  @BeforeClass
  public static void setUpClass() {
    DockerSetup.startDocker();
  }
  
  @AfterClass
  public static void tearDownClass() {
    DockerSetup.stopDocker();
  }
  
  @Test
  public void testTest() {
    
  }
}
