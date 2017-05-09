package sjchat.users;

import io.grpc.stub.StreamObserver;

import java.util.Random;

import sjchat.daos.UserDao;
import sjchat.daos.UserDaoImpl;
import sjchat.entities.UserEntity;

import sjchat.users.UserAuthentication;
import sjchat.users.tokens.AuthenticationResult;


class UserService extends UserServiceGrpc.UserServiceImplBase {
  UserDao dao = new UserDaoImpl();
  @Override
  public void createUser(CreateUserRequest req, StreamObserver<CreateUserResponse> responseObserver) {
    UserEntity entity = new UserEntity(null, req.getUsername());
    //TODO: Check that no user with this username exists (kundera should throw an exception I think?)
    dao.create(entity);
    entity = dao.findByUsername(req.getUsername());

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
    AuthenticationResult authResult = UserAuthentication.getInstance().verifyCredentials(req.username, req.password);
    LoginResponse.Builder loginResponse = LoginResponse.newBuilder().setAuthenticated(authResult.success)
    if(authResult.success) {
      loginResponse.setErrorMessage(authResult.message);
    } else {
      loginResponse.setToken(authResult.token);
    }

    responseObserver.onNext(loginResponse);
    responseObserver.onCompleted();
  }

  /**
   * Logout from all devices
   */
  public void logout(AuthRequest req, StreamObserver<LogoutResponse> responseObserver) {
    LogoutResponse logoutResponse = LogoutResponse
      .newBuilder().setAuthenticated(true).build();
    responseObserver.onCompleted();
  }

  public void validateToken(AuthRequest req, StreamObserver<ValidateTokenResponse> responseObserver) {
    String username = req.getUsername();
    String token = req.getToken();
    AuthenticationResult authResult = UserAuthentication.getInstance().authenticateToken(username, token);
  }

}
