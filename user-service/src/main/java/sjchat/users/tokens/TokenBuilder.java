package sjchat.users.tokens;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
public class TokenBuilder {

  private final String secret;
  private final String issuer;

  public TokenBuilder(String issuer, String secret) {
    this.secret = secret;
    this.issuer = issuer;
  }

  public String build(String subject, Date expiration) {
    return Jwts.builder()
      .setSubject(subject)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(expiration)
      .setIssuer(this.issuer)
      .signWith(SignatureAlgorithm.HS512, this.secret)
      .compact();
  }
}
