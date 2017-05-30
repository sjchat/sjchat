package sjchat.users.tokens;

import java.util.Date;

public class TokenizationData {
  public final String token;
  public final Date issuedAt;
  public final Date expiration;

    

  public TokenizationData(String token, Date issuedAt, Date expiration) {
    this.token = token;
    this.issuedAt = issuedAt;
    this.expiration = expiration;
  }

}

