package sjchat.users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.lang.Exception;
import java.util.Date;

import sjchat.daos.UserDao;
import sjchat.daos.UserDaoImpl;
import sjchat.entities.UserEntity;
import sjchat.exceptions.NoEntityExistsException;

import sjchat.users.exceptions.AuthenticationException;
import sjchat.users.tokens.*;

public class UserAuthentication {
  private TokenConfig.Configurations config;
  private TokenAuth auth;
  private TokenBuilder builder;
  private final Date nulldate;
  private final UserDao dao;

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
    this.nulldate = new Date(0);
    this.dao = new UserDaoImpl();
  }


  public static UserAuthentication getInstance() {
    return UserAuthenticationHolder.INSTANCE;
  }


  private static class UserAuthenticationHolder {
    private static final UserAuthentication INSTANCE = new UserAuthentication();
  }

  public UserAuthentication checkUserExists(String username, String password) throws AuthenticationException {
    UserEntity user = dao.findByUsername(username);

    if (user == null)
      throw new AuthenticationException("Bad username");

    if (user.getPassword() != password)
      throw new AuthenticationException("Bad password");

    return this;
  }

  public UserAuthentication checkUsernameExists(String username) throws AuthenticationException {
    UserEntity user = dao.findByUsername(username);

    if (user == null)
      throw new AuthenticationException("Bad username");

    return this;
  }

  public TokenizationData tokenizeUser(String username) {
    Date expiration = config.getExpiration();
    Date issuedAt = new Date(System.currentTimeMillis());

    return new TokenizationData(this.builder.build(username, issuedAt, expiration), issuedAt, expiration);
  }




  public TokenizationData authenticateUser(String username, String serializedToken) throws AuthenticationException {
    Jws<Claims> token;

    try {
      token = this.auth.authenticate(username, serializedToken);
    } catch (Exception e) {
      throw new AuthenticationException(e.getMessage());
    }

    return new TokenizationData(serializedToken, token.getBody().getIssuedAt(), token.getBody().getExpiration());

  }

  public UserUpdater updateUser(String username) throws NoEntityExistsException {
    return new UserUpdater(dao.findByUsername(username), dao);
  }


  static class UserUpdater {
  
    public final UserEntity user;
    private final UserDao dao;

    public UserUpdater(UserEntity user, UserDao dao) {
      this.user = user;
      this.dao = dao;
    }

    public UserUpdater setLastForcedLogout(Date date) {
      this.user.setLastForcedLogout(date);

      return this;
    }

    public UserUpdater setLastForcedLogoutNow() {
      this.user.setLastForcedLogout(new Date(System.currentTimeMillis()));
      
      return this;
    }


    public void update() throws NoEntityExistsException {
      dao.update(this.user);
    }
  }
}
