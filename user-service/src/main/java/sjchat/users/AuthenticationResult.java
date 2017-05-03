package sjchat.users;

public class AuthenticationResult {
    
    public final boolean result;
    public final String message;


    public AuthenticationResult(boolean result, String message) {
        this.message = message;
        this.result = result;
    }
}
