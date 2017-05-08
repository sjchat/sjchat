package sjchat.users.tokens;

import io.jsonwebtoken.Jwts;

public class TokenAuth {

    private final String issuer;
    private final String secret;

    public TokenAuth(String issuer, String secret) {
        this.issuer = issuer;
        this.secret = secret;
    }

    public String authenticate(String jws, String subject) {
         return Jwts.parser()
             .requireIssuer(this.issuer)
             .setSigningKey(this.secret)
             .requireSubject(subject)
             .parseClaimsJws(jws)
             .getBody()
             .getSubject() + " Authenticated Successfully";
    }
}
