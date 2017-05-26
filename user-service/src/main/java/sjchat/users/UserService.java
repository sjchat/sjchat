package sjchat.users;

import io.grpc.stub.StreamObserver;

import java.util.Date;
import java.util.Random;

import sjchat.daos.UserDao;
import sjchat.daos.UserDaoImpl;
import sjchat.entities.UserEntity;
import sjchat.exceptions.NoEntityExistsException;

import sjchat.users.UserAuthentication;
import sjchat.users.exceptions.AuthenticationException;


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

    CreateUserResponse userResponse = CreateUserResponse.newBuilder().setUser(userBuilder.build()).build();
    
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
    LoginResponse loginResponse;

    try {
      loginResponse = LoginResponse
        .newBuilder()
        .setAuthenticated(true)
        .setToken(UserAuthentication
        .getInstance()
        .checkUserExists(req.getUsername(), req.getPassword())
        .tokenizeUser(req.getUsername()).token)
        .build();

    } catch (AuthenticationException e) {
      loginResponse = LoginResponse
        .newBuilder()
        .setAuthenticated(false)
        .setErrorMessage(e.getMessage())
        .build();
    }

    responseObserver.onNext(loginResponse);
    responseObserver.onCompleted();
  }

  public void logout (AuthRequest req, StreamObserver<LogoutResponse> responseObserver) {
    LogoutResponse logoutResponse;
    try {
      UserAuthentication
        .getInstance()
        .checkUsernameExists(req.getUsername())
        .authenticateUser(req.getUsername(), req.getToken());

      UserAuthentication
        .getInstance()
        .updateUser(req.getUsername())
        .setLastForcedLogoutNow()
        .update();
      
     logoutResponse = LogoutResponse
     .newBuilder()
     .setAuthenticated(true)
     .build();

    } catch (AuthenticationException | NoEntityExistsException e) {
      logoutResponse = LogoutResponse
        .newBuilder()
        .setAuthenticated(false)
        .setErrorMessage(e.getMessage())
        .build();
    }
    
    responseObserver.onNext(logoutResponse);
    responseObserver.onCompleted();
  }

  
  public void validateToken(AuthRequest req, StreamObserver<ValidateTokenResponse> responseObserver) {
    ValidateTokenResponse validateTokenResponse;

    try {
      UserAuthentication
        .getInstance()
        .authenticateUser(req.getUsername(), req.getToken());
      
      validateTokenResponse = ValidateTokenResponse
        .newBuilder()
        .setValid(true)
        .build();
    
    } catch (AuthenticationException e) {
      validateTokenResponse = ValidateTokenResponse
        .newBuilder()
        .setValid(false)
        .build();
    }

    responseObserver.onNext(validateTokenResponse);
    responseObserver.onCompleted();
  }

}
