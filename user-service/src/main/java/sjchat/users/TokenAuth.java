package sjchat.users;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
public class SJJwtAuthentication {
    
    private final String issuer;
    private final byte[] secret;

    public SJJwtAuthentication(String issuer, byte[] secret) {
        this.issuer = issuer;
        this.secret = secret;
    }

    public String authenticate(String jws, String subject) {
         return Jwts.parser()
             .requireIssuer(this.issuer)
             .setSigningKey(this.secret)
             .requireSubject(subject)
             .parseClaimsJws()
             .getBody()
             .getSubject() + " Authenticated Successfully";
    }
}


