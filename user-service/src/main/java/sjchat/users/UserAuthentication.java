package sjchat.users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;


import java.lang.Exception;
import java.util.Date;
import sjchat.users.tokens.*;

public class UserAuthentication {
    private final TokenConfig.Configurations config;
    private final TokenAuth auth;
    private final TokenBuilder builder;
    private final Date nulldate = new Date(0);
    
    public String tokenize(String username) {
        return this.builder.build(username, config.getExpiration());
    }
    
    public AuthenticationResult authenticate(String username, String jws) {
        try {
            Jws<Claims> token = this.auth.authenticate(username, jws);
            
            return new AuthenticationResult(token.getBody().getSubject() 
                    + " Authenticated Successfully "
                    + " token expires at "
                    + token.getBody().getExpiration().toGMTString(), true, token.getBody().getIssuedAt());

        } catch (Exception e) {
            return new AuthenticationResult(e.getMessage(), false, nulldate);
        }
    }
    
    private UserAuthentication() {
        this.config = TokenConfig.get();
        this.auth = new TokenAuth(this.config.issuer, this.config.secret);
        this.builder = new TokenBuilder(this.config.issuer, this.config.secret); 
    }

    
    public static UserAuthentication getInstance() {
        return UserAuthenticationHolder.INSTANCE;
    }
    
    
    private static class UserAuthenticationHolder {
        private static final UserAuthentication INSTANCE = new UserAuthentication();
    }
}

