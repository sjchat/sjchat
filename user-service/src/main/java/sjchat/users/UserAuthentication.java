package sjchat.users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.lang.Exception;
import java.util.Date;
import sjchat.users.tokens.*;

import sjchat.daos.UserDao;
import sjchat.daos.UserDaoImpl;
import sjchat.entities.UserEntity;

public class UserAuthentication {
  private TokenConfig.Configurations config;
  private TokenAuth auth;
  private TokenBuilder builder;
  private final Date nulldate = new Date(0);
  private final UserDao dao = new UserDaoImpl();

  public void updateConfiguration() {
    TokenConfig.refreshConfiguration();
    this.config = TokenConfig.get();
    this.auth = new TokenAuth(this.config.issuer, this.config.secret);
    this.builder = new TokenBuilder(this.config.issuer, this.config.secret);
  }

  private UserAuthentication() {
    this.config = TokenConfig.get();
    this.auth = new TokenAuth(this.config.issuer, this.config.secret);
    this.builder = new TokenBuilder(this.config.issuer, this.config.secret);
  }


  public static UserAuthentication getInstance() {
    return UserAuthenticationHolder.INSTANCE;
  }


  private static class UserAuthenticationHolder {
    private static final UserAuthentication INSTANCE = new UserAuthentication();
  }

  public AuthenticationResult verifyCredentials(String username, String password) {
    UserEntity user = dao.findByUsername(username);
    Date expiration = config.getExpiration();

    if (user == null)
      return new AuthenticationResult("User does not exist", false, nulldate, "");

    if (user.getPassword() == password)
      return new AuthenticationResult("Authentication successful", true, expiration, this.tokenize(username));

    return new AuthenticationResult("Wrong Password", false, nulldate, "");

  }

  public String tokenize(String username) {
    return this.builder.build(username, config.getExpiration());
  }

  public AuthenticationResult authenticateToken(String username, String serializedToken) {
    Jws<Claims> token;
    try {
      token = this.auth.authenticate(username, serializedToken);
    } catch (Exception e) {
      return new AuthenticationResult(username
                                      + " failed to authenticate, reason being:  "
                                      + e.getMessage(), false, nulldate, "");
    }

    return new AuthenticationResult(token.getBody().getSubject()
                                    + " Authenticated Successfully "
                                    + " token expires at "
                                    + token.getBody().getExpiration().toGMTString(),
                                    true,
                                    token.getBody().getIssuedAt(), serializedToken);
  }
}
