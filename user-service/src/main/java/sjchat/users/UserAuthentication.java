package sjchat.users;

import io.jsonwebtoken.*;
import java.util.Date;
import sjchat.users.SJJwtAuthentication;
import sjchat.users.SJJwtBuilder;
import sjchat.users.AuthenticationResult;
import sjchat.restapi.entities.User;
public class UserAuthentication {
    
    private SJJwtAuthentication JWSauth;
    private SJJwtBuilder JWSbuilder;


    public UserAuthentication(String issuer, byte[] secret) {
        this.auth = new SJJwtAuthentication(issuer, secret);
        this.builder = new SJJwtBuilder(issuer, secret);
    }

    String tokenize(User user, Date expiration) {
        return this.JWSbuilder.build(user.getUsername(), expiration);
    }

    AuthenticationResult authenticate(User user, String jws) {
        try {
           return new AuthenticationResult(this.JWSauth.authenticate(user.getUsername(), jws), true);
        } catch (Exception e) {
            return new AuthenticationResult(e.getMessage(), false);
        }
    }
}
