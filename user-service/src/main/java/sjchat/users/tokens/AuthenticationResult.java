package sjchat.users.tokens;

import java.util.Date;

public class AuthenticationResult {
    
    public final String message;
    public final boolean result;
    public final Date issuedAt;

    public AuthenticationResult(String message, boolean result, Date issuedAt) {
        this.message = message;
        this.result = result;
        this.issuedAt = issuedAt;
    }
}
