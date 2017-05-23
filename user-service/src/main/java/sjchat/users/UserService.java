package sjchat.users;

import io.grpc.stub.StreamObserver;

import java.util.Date;
import java.util.Random;

import sjchat.daos.UserDao;
import sjchat.daos.UserDaoImpl;
import sjchat.entities.UserEntity;
import sjchat.exceptions.NoEntityExistsException;

import sjchat.users.UserAuthentication;
import sjchat.users.tokens.AuthenticationResult;


class UserService extends UserServiceGrpc.UserServiceImplBase {
  UserDao dao = new UserDaoImpl();
  
  //returns null id if user already exist, i am assuming that dao.create assigns an ID.
  @Override
  public void createUser(CreateUserRequest req, StreamObserver<CreateUserResponse> responseObserver) {
    

    UserEntity entity = new UserEntity(null, req.getUsername());

    if (dao.findByUsername(req.getUsername()) == null) {
      dao.create(entity);
      entity = dao.findByUsername(req.getUsername());

    }

    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId(entity.getId());
    userBuilder.setUsername(entity.getUsername());

    CreateUserResponse userResponse = CreateUserResponse.newBuilder().setUser(userBuilder).build();
    
    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUser(GetUserRequest req, StreamObserver<GetUserResponse> responseObserver) {
    UserEntity entity = dao.find(req.getId());
    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId(entity.getId());
    userBuilder.setUsername(entity.getUsername());

    GetUserResponse userResponse = GetUserResponse.newBuilder().setUser(userBuilder).build();

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  //What's this supposed to do?
  @Override
  public void updateUser(UpdateUserRequest req, StreamObserver<UpdateUserResponse> responseObserver) {

    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId(req.getId());
    userBuilder.setUsername(req.getUsername());

    UpdateUserResponse userResponse = UpdateUserResponse.newBuilder().setUser(userBuilder).build();

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  public void loginUserPassword(LoginRequest req, StreamObserver<LoginResponse> responseObserver) {
    AuthenticationResult authResult = UserAuthentication.getInstance().verifyCredentials(req.getUsername(), req.getPassword());
    LoginResponse.Builder loginResponse = LoginResponse.newBuilder().setAuthenticated(authResult.success);
    if(authResult.success) {
      loginResponse.setErrorMessage(authResult.message);
    } else {
      loginResponse.setToken(authResult.token);
    }

    responseObserver.onNext(loginResponse.build());
    responseObserver.onCompleted();
  }

  /**
   * Logout from all devices
   */
  public void logout(AuthRequest req, StreamObserver<LogoutResponse> responseObserver) {
    UserEntity userEntity = dao.findByUsername(req.getUsername());
    LogoutResponse logoutResponse;
    if (userEntity == null) {
      logoutResponse = LogoutResponse.newBuilder().setAuthenticated(false).setErrorMessage("No such username").build();
    } else if (UserAuthentication.getInstance().authenticateToken(req.getUsername(), req.getToken()).success) {
      logoutResponse = LogoutResponse.newBuilder().setAuthenticated(true).build();
      userEntity.setLastForcedLogout(new Date());
      try {
        dao.update(userEntity);
      } catch (NoEntityExistsException e){}
    } else {
      logoutResponse = LogoutResponse.newBuilder().setAuthenticated(false).setErrorMessage("Token expired").build();
    }
    responseObserver.onNext(logoutResponse);
    responseObserver.onCompleted();
  }

  public void validateToken(AuthRequest req, StreamObserver<ValidateTokenResponse> responseObserver) {
    String username = req.getUsername();
    String token = req.getToken();
    AuthenticationResult authResult = UserAuthentication.getInstance().authenticateToken(username, token);
  }

}
