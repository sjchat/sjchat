package sjchat.users;

import java.lang.Exception;
import java.util.Date;

import sjchat.users.tokens.*;

public class UserAuthentication {

    private final TokenConfig.Configurations config;
    private final TokenAuth auth;
    private final TokenBuilder builder;
    
    public String tokenize(String username) {
        return this.builder.build(username, config.getExpiration());
        
    }

    public AuthenticationResult authenticate(String username, String jws) {
        try {
            return new AuthenticationResult(this.auth.authenticate(jws, username), true);
        } catch (Exception e) {
            return new AuthenticationResult(e.getMessage(), false);
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

