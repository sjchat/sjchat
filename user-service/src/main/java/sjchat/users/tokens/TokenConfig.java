package sjchat.users.tokens;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class TokenConfig {

  static final String CONFIGFILE = "TokenConfig.properties";
  static final Properties properties = new Properties();


  static {
    try {
      properties.load(TokenConfig.class.getClassLoader().getResourceAsStream(CONFIGFILE));
      System.err.println("Configurations has been properly loaded");
    } catch (IOException e) {
      System.err.println("Unable to Load Resource: " + CONFIGFILE + " Error: " + e.getMessage()
                         + "\nWARNING: Tokenization & Authentication features will not work");
    }
  }

  public static Configurations get() {
    return new Configurations(properties.getProperty("issuer"), properties.getProperty("expiration"), properties.getProperty("secret"));
  }

  public static void refreshConfiguration() {
    try {
      properties.load(TokenConfig.class.getClassLoader().getResourceAsStream(CONFIGFILE));
      System.err.println("Configurations has been properly loaded");
    } catch(IOException e) {
      System.err.println("Unable to Load Resource: " + CONFIGFILE + " Error: " + e.getMessage());
    }
  }

  public static class Configurations {
    public final String issuer;
    public final String secret;
    public final long tokenDefaultExpiration;

    public Configurations(String issuer, String tokenDefaultExpiration, String secret) {
      this.issuer = issuer;
      this.secret = secret;
      this.tokenDefaultExpiration = Long.parseLong(tokenDefaultExpiration);
    }

    public Date getExpiration() {
      return new Date(System.currentTimeMillis() + tokenDefaultExpiration);
    }

  }

}
