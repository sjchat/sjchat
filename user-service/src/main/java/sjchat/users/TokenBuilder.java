package sjchat.users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
public class SJJwtBuilder {

    private final byte[] secret;
    private final String issuer;

    public SJJwtBuilder(String issuer, byte[] secret) {
        this.secret = secret;
        this.issuer = issuer;
    }

    public String build(String subject, Date expiration) {
        return Jwts.builder()
            .setSubject(subject)
            .setExpiration(expiration)
            .setIssuer(this.issuer)
            .signWith(SignatureAlgorithm.HS512, this.secret)
            .compact();
    }
}

