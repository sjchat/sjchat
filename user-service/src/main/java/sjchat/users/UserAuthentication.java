package sjchat.users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;


import java.lang.Exception;
import java.util.Date;
import sjchat.users.tokens.*;
import sjchat.users.entities.User;


public class UserAuthentication {
  private TokenConfig.Configurations config;
  private TokenAuth auth;
  private TokenBuilder builder;
  private final Date nulldate = new Date(0);

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
  public User tokenize(User user) {
    user.jws = this.builder.build(user.username, config.getExpiration());

    return user;
  }

  public String tokenize(String username) {
    return this.builder.build(username, config.getExpiration());
  }

  public AuthenticationResult authenticateUser(User user) {
    return authenticateToken(user.username, user.jws);
  }

  public AuthenticationResult authenticateToken(String username, String serializedToken) {
    Jws<Claims> token;
    try {
      token = this.auth.authenticate(username, serializedToken);
    } catch (Exception e) {
      return new AuthenticationResult(username
                                      + " failed to authenticate, reason being:  "
                                      + e.getMessage(), false, nulldate);
    }

    return new AuthenticationResult(token.getBody().getSubject()
                                    + " Authenticated Successfully "
                                    + " token expires at "
                                    + token.getBody().getExpiration().toGMTString(),
                                    true,
                                    token.getBody().getIssuedAt());
  }
}
