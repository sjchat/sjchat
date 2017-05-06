package sjchat.users;

import java.util.Date;
import sjchat.users.AuthenticationResult;
import sjchat.restapi.entities.User;
import sjchat.users.TokenAuth;
import sjchat.users.TokenBuilder;
public class UserAuthentication {
    
    private TokenAuth auth;
    private TokenBuilder builder;
    


    public UserAuthentication(String issuer, byte[] secret) {
        this.auth = new TokenAuth(issuer, secret);
        this.builder = new TokenBuilder(issuer, secret);
    }

    String tokenize(User user, Date expiration) {
        return this.builder.build(user.getUsername(), expiration);
    }

    AuthenticationResult authenticate(User user, String jws) {
      
        try {
     
            return new AuthenticationResult(this.auth.authenticate(user.getUsername(), jws), true);
      
        } catch (Exception e) {
      
            return new AuthenticationResult(e.getMessage(), false);
      
        }
    }
}
