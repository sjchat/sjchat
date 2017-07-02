package sjchat.general.testutils;

/**
 * Created by jovi on 2017-07-02.
 */
import io.grpc.stub.StreamObserver;

public class BasicStreamObserverImpl<V> implements StreamObserver<V> {
  V lastentity = null;
  Throwable lastthrowable = null;
  boolean completed = false;
  public void reset(){
    lastentity = null;
    completed = false;
  }
  public void onCompleted(){
    completed = true;
  }

  public void onNext(V entity){
    lastentity = entity;
  }

  public void onError(Throwable throwable){
    lastthrowable = throwable;
  }
  public Throwable getLastThrowable(){
    return lastthrowable;
  }
  public V getLastEntity(){
    return lastentity;
  }
}