package sjchat.users;

import java.io.IOException;
import java.util.Random;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sjchat.storage.UserDao;
import sjchat.storage.UserDaoImpl;

public class UserServiceServer {
  private Server server;
  private int port = 50051;

  public static void main(String[] args) throws Exception {
    UserServiceServer userServiceServer = new UserServiceServer();
    userServiceServer.start();
    userServiceServer.blockUntilShutdown();
  }


  private void start() throws IOException {
    server = ServerBuilder.forPort(port).addService(new UserService()).build().start();
    System.out.println("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        UserServiceServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  static class UserService extends UserServiceGrpc.UserServiceImplBase {
    private UserDao userDao;

    public UserService(){
      super();
      userDao = new UserDaoImpl();
      //ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
      //service = ctx.getBean(sjchat.storage.UserService.class);
      /*
      ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application-context.xml");
Calculator calculator = ctx.getBean(Calculator.class);

      * */
    }

    @Override
    public void createUser(UserDataRequest req, StreamObserver<UserResponse> responseObserver) {
      Random random = new Random();

      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(Math.abs(random.nextInt(100)));
      userBuilder.setUsername(req.getUsername());

      UserResponse userResponse = UserResponse.newBuilder().setUser(userBuilder).build();

      //If a user with this username already exists this function will throw.
      userDao.create(sjchat.restapi.entities.User.buildUser(userBuilder.build()));

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void getUser(UserRequest req, StreamObserver<UserResponse> responseObserver) {
      sjchat.restapi.entities.User user = userDao.findById(req.getId()); //service.findById(req.getId());
      User.Builder userBuilder = User.newBuilder();

      if(user != null){
        userBuilder.setId(user.getId());
        userBuilder.setUsername(user.getUsername());
      }

      UserResponse userResponse = UserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UserDataRequest req, StreamObserver<UserResponse> responseObserver) {
      sjchat.restapi.entities.User user = new sjchat.restapi.entities.User(req.getId(), req.getUsername());
      sjchat.restapi.entities.User updated = userDao.update(user); //service.update(user);

      User.Builder userBuilder = User.newBuilder();
      userBuilder.setId(updated.getId());
      userBuilder.setUsername(updated.getUsername());

      UserResponse userResponse = UserResponse.newBuilder().setUser(userBuilder).build();

      responseObserver.onNext(userResponse);
      responseObserver.onCompleted();
    }
  }
}
