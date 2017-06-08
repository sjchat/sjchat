package sjchat.users.tokens;

import java.util.Date;

public class TokenizationData {
  public final String token;
  public final String username;
  public final Date issuedAt;
  public final Date expiration;

    

  public TokenizationData(String token, String username, Date issuedAt, Date expiration) {
    this.token = token;
    this.username = username;
    this.issuedAt = issuedAt;
    this.expiration = expiration;
  }

}

