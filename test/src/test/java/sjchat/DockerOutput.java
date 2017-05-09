package sjchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class DockerOutput {
  
  private final StreamPrinter in, err;
  private Thread inThread, errThread;
  private boolean stop = false;
  
  public DockerOutput(Process p) {
    this(p.getInputStream(), p.getErrorStream());
  }
  
  public DockerOutput(InputStream in, InputStream err) {
    this.in = new StreamPrinter(in, System.out);
    this.err = new StreamPrinter(err, System.err);
  }
  
  public void start() {
    inThread = new Thread(in);
    inThread.start();
    
    errThread = new Thread(err);
    errThread.start();
  }
  
  public void stop() {
    stop = true;
    
    inThread.interrupt();
    errThread.interrupt();
  }
  
  private class StreamPrinter implements Runnable {
    
    private BufferedReader input;
    private PrintStream output;
    
    private StreamPrinter(InputStream input, PrintStream output) {
      this.input = new BufferedReader(new InputStreamReader(input));
      this.output = output;
    }
    
    @Override
    public void run() {
      try {
        while (!stop) {
          String line = input.readLine();
          
          if (line == null || stop) break;
          
          output.append(line).append('\n');
          output.flush();
        }
      } catch (IOException e)  {
        throw new RuntimeException(e.getMessage(), e);
      } finally {
        try {
          input.close();
        } catch (IOException e2) {
          System.err.println("Error closing input stream: " + e2.getMessage());
        }
      }
    }
  }
}
