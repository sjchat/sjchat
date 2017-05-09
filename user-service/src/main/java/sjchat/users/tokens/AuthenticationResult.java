package sjchat.users.tokens;

import java.util.Date;

public class AuthenticationResult {

  public final String message;
  public final boolean success;
  public final Date issuedAt;
  public final String token;

  public AuthenticationResult(String message, boolean success, Date issuedAt, String token) {
    this.message = message;
    this.success = success;
    this.issuedAt = issuedAt;
    this.token = token;
  }
}
