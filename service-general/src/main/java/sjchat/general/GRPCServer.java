package sjchat.general;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GRPCServer {
  private Server server;
  private io.grpc.BindableService service;
  private int port;

  public GRPCServer(io.grpc.BindableService service, int port) {
    this.service = service;
    this.port = port;
  }

  public void start() throws Exception {
    server = ServerBuilder.forPort(port).addService(service).build().start();
    System.out.println("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.out.println("*** shutting down gRPC server since JVM is shutting down");
        GRPCServer.this.stop();
        System.out.println("*** server shut down");
      }
    });
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }
}
