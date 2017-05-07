package sjchat.users;

import java.util.Date;
import sjchat.users.tokens*;
import java.lang.Exception;


public class UserAuthentication {

    private TokenAuth auth;
    private TokenBuilder builder;
    private TokenConfig.Configurations configurations;

    public UserAuthentication(TokenConfig.Configurations configurations) {
        this.auth = new TokenAuth(configurations.issuer, configurations.secret);
        this.builder = new new TokenBuilder(configurations.issuer, configurations.secret);
    }

    String tokenize(String username, Date tokenExpiration) {
        return this.builder.build(username, expiration);
    }

    String tokenize(String username) {
        return this.builder.build(username, configurations.getExpiration());
    }

    AuthenticationResult authenticate(String username, String jws) {

        try {
            
            return new AuthenticationResult(this.auth.authenticate(username, jws), true);

        } catch (Exception e) {

            return new AuthenticationResult(e.getMessage(), false);

        }
    }
}
