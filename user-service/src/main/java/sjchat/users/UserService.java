package sjchat.users;

import java.util.Random;

import io.grpc.stub.StreamObserver;
import sjchat.daos.UserDao;
import sjchat.daos.UserDaoImpl;
import sjchat.entities.UserEntity;

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
}
