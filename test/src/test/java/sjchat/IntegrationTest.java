package sjchat;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntegrationTest {
  
  public IntegrationTest() {}
  
  private static final String DOCKER_COMPOSE_PATH = "src/test/resources/docker-compose-test.yaml";
  
  @BeforeClass
  public static void setUpClass() {
    System.out.println("Starting docker...");
    try {
      Process p = Runtime.getRuntime().exec("docker stack deploy -c \"" + DOCKER_COMPOSE_PATH + "\" sjchat-test");
      DockerOutput d = new DockerOutput(p);
      d.start();
      
      try {
        p.waitFor();
      } catch (InterruptedException e) {}
      
      d.stop();
      
    } catch (IOException e) {
      throw new RuntimeException("Docker failed to start.", e);
    }
  }
  
  @AfterClass
  public static void tearDownClass() {
    try {
      Process p = Runtime.getRuntime().exec("docker stack rm sjchat-test");
      DockerOutput d = new DockerOutput(p);
      d.start();
      
      try {
        p.waitFor();
      } catch (InterruptedException e) {}
      
      d.stop();
    } catch (IOException e) {
      throw new RuntimeException("Docker failed to exit.", e);
    }
  }
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testTest() {
    
  }
}
