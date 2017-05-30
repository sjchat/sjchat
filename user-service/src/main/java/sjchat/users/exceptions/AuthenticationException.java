package sjchat.users.exceptions;

public class AuthenticationException extends Exception {

  public AuthenticationException() {}

  public AuthenticationException(String msg) {
    super(msg);
  }

  public String getMessage() {
    return super.getMessage();
  }

}

