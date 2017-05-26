package sjchat.users.tokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class TokenAuth {

  private final String issuer;
  private final String secret;

  public TokenAuth(String issuer, String secret) {
    this.issuer = issuer;
    this.secret = secret;
  }

  public Jws<Claims> authenticate(String username, String jws) {
    return Jwts.parser()
      .requireIssuer(this.issuer)
      .setSigningKey(this.secret)
      .requireSubject(username)
      .parseClaimsJws(jws);

  }
}
