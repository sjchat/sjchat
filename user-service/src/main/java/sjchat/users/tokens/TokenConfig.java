package sjchat.users.tokens;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

class TokenConfig {
    
    static final String CONFIGFILE = "TokenConfig.Properties";
    static final Properties properties;


    static {
        InputStream in;
        try {
            in = TokenConfig.class.getClassLoader().getResourceAsStream(CONFIGFILE);
        } catch (IOException e) {
            System.err.println("Unable to Load Resource: " + CONFIGFILE);
        }
    }

    static Configurations get() {
        return new Configurations(properties.get("issuer"), properties.get("expiration"), properties.get("secret");
    }

    static class Configurations {
        public final String issuer;
        public final String secret;
        public final long long tokenDefaultExpiration;
        
        public Configurations(String issuer, long long tokenDefaultExpiration, String secret) {
            this.issuer = issuer;
            this.secret = secret;
            this.tokenDefaultExpiration = tokenDefaultExpiration;
        }

        public Date getExpiration() {
            return new Date(System.currentTimeMillis() + tokenDefaultExpiration);
        }

    }

}



