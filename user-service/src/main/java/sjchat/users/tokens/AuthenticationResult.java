package sjchat.users.tokens;

public class AuthenticationResult {
    
    public final String message;
    public final boolean result;



    public AuthenticationResult(String message, boolean result) {
        this.message = message;
        this.result = result;
    }

}
