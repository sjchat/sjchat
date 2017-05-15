package sjchat.restapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingOrEmptyParameterException extends RuntimeException {
  public MissingOrEmptyParameterException() {
    super();
  }
  public MissingOrEmptyParameterException(String message, Throwable cause) {
    super(message, cause);
  }
  public MissingOrEmptyParameterException(String message) {
    super(message);
  }
  public MissingOrEmptyParameterException(Throwable cause) {
    super(cause);
  }
}