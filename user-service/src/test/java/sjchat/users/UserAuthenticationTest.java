package sjchat.users;

import java.io.IOException;
import java.util.Date;
import org.junit.Test;
import sjchat.users.tokens.*;
import static org.junit.Assert.*;

public class UserAuthenticationTest {

  @Test
    public void TokenizationAndAuthentication() throws IOException {
  
      TokenConfig.Configurations config = TokenConfig.get();

      TokenAuth tokenauth = new TokenAuth(config.issuer, config.secret);
      TokenBuilder tokenbuilder = new TokenBuilder(config.issuer, config.secret);
      
      String username = "Kalle";
      String token = tokenbuilder.build(username, new Date(System.currentTimeMillis()), config.getExpiration());

      try {
        assertNotNull(tokenauth.authenticate(username, token));
      } catch (Exception e) {
        assertTrue(false);
      }
    }
}

