package sjchat.users;

public class AuthResponse<T> {
  private T payload = null;
  private String errorMessage = null;
  private bool authenticated;

  public class AuthResponse(bool authenticated) {
    this.authenticated = authenticated;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }

  public T getPayload() {
    return payload;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
