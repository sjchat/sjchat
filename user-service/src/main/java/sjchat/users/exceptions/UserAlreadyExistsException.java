package sjchat.users.exceptions;

/**
 * Created by jovi on 2017-05-11.
 */
public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException(){
    super("User already exists");
  }
}
