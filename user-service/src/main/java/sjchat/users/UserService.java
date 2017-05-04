package sjchat.users;

import java.util.Random;

import io.grpc.stub.StreamObserver;

class UserService extends UserServiceGrpc.UserServiceImplBase {

  @Override
  public void createUser(CreateUserRequest req, StreamObserver<CreateUserResponse> responseObserver) {
    Random random = new Random();

    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId(null);
    userBuilder.setUsername(req.getUsername());

    CreateUserResponse userResponse = CreateUserResponse.newBuilder().setUser(userBuilder).build();

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUser(GetUserRequest req, StreamObserver<GetUserResponse> responseObserver) {
    User.Builder userBuilder = User.newBuilder();
    userBuilder.setId(req.getId());
    userBuilder.setUsername("user_" + req.getId());

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
