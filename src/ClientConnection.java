import java.io.IOException;
import java.net.Socket;


public class ClientConnection extends Thread  {
  // variable to store opened client socket
  Socket socket = null;
  //to notify when server connection is closed and we need to stop thread
  boolean keepWorking = true;
  
  public ClientConnection(Socket socket) {
    // run constructor of parent class
    super("ClientConnection");
    // save client socket
    setSocket(socket);
  }

  public void run() {
    //Process client connection
    String message = "";
    
    // listening to messages from the server and print them to the user
    while(keepWorking){
      try {
        // set socket timeout to 1 second that we can stop thread safely after all
        socket.setSoTimeout(1000);
        try {
          message = Client.in.readLine();
          if (message == null) {
            throw new IOException();
          }
          System.out.print(message + "\n>> ");
        } catch (java.net.SocketTimeoutException e) {
          // 1000ms have elapsed but nothing was read
          // just move to the next iteration of the cycle
        }
      } catch (IOException e) {
        //new RuntimeException(e);
        System.out.println("Server was disconnected");
        System.out.print(">> ");
        keepWorking = false;
      } 
    }
    
    try {
      socket.close();
    } catch (IOException e) {
      //new RuntimeException(e);
      System.out.println("Socket was already closed");
    }
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

}
