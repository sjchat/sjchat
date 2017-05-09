package sjchat;

import java.io.IOException;

public final class DockerSetup {
  
  private DockerSetup() {}
  
  private static final String DOCKER_COMPOSE_PATH = "src/test/resources/docker-compose-test.yaml";
  
  public static void startDocker() {
    startDocker(DOCKER_COMPOSE_PATH);
  }
  
  public static void startDocker(String composePath) {
    System.out.println("Starting docker...");
    try {
      Process p = Runtime.getRuntime().exec("docker stack deploy -c \"" + composePath + "\" sjchat-test");
      OutputPrinter d = new OutputPrinter(p);
      d.start();
      
      try {
        p.waitFor();
      } catch (InterruptedException e) {}
      
      d.stop();
      
    } catch (IOException e) {
      throw new RuntimeException("Docker failed to start.", e);
    }
  }
  
  public static void stopDocker() {
    try {
      Process p = Runtime.getRuntime().exec("docker stack rm sjchat-test");
      OutputPrinter d = new OutputPrinter(p);
      d.start();
      
      try {
        p.waitFor();
      } catch (InterruptedException e) {}
      
      d.stop();
    } catch (IOException e) {
      throw new RuntimeException("Docker failed to exit.", e);
    }
  }
}
