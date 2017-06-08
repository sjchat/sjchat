package sjchat.users;

import java.io.IOException;
import java.util.Properties;

public class UserServerConfig {
  
  static final String CONFIG_FILE = "server-config.properties";
  static final Properties props = new Properties();
  
  static {
    try {
      props.load(UserServerConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
      System.out.println("Server config loaded.");
    } catch (IOException e) {
      throw new RuntimeException("Failed to load server config: " + e.getMessage());
    }
  }
  
  public static final int getPort() {
    return Integer.parseInt(props.getProperty("port"));
  }
}
