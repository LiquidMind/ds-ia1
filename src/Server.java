import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;

public class Server extends Thread {
  // opened server socket
  static ServerSocket serverSocket = null;
  // port that server is listening to
  static int serverPort = 2333;
  //flag to notify that server should keep working
  static boolean keepWorking = true;
  //scanner to read user input
  private static Scanner userInput = null;
  // hash table to save all connected clients
  static HashSet<ServerConnection> clients = new HashSet<ServerConnection>();

  
  // separate thread to process new connections
  public void run(){
    Socket clientSocket;
    ServerConnection serverConnection;    
    while(keepWorking) {
      try {
        serverSocket.setSoTimeout(1000);
        try {
          clientSocket = serverSocket.accept();
          serverConnection = new ServerConnection(clientSocket);
          addClient(serverConnection);
          
          System.out.print("New client " + serverConnection.hashCode() + " was connected" + "\n>> ");

          serverConnection.start();
        } catch (java.net.SocketTimeoutException e) {
          // 1000ms have elapsed but nothing was read
          // just move to the next iteration of the cycle
        }
      } catch (IOException e) {
        new RuntimeException(e);
      }      
    }    
  }
  
  public static void main(String[] args) throws IOException {
    // Display server IP addresses that clients can connect to
    System.out.println("Server IP: " + InetAddress.getLocalHost().getHostAddress());
    System.out.println("Server IPv4: " + Inet4Address.getLocalHost().getHostAddress());
    System.out.println("Server IPv6: " + Inet6Address.getLocalHost().getHostAddress());
    System.out.println("Use one of these IP addresses to run client using \"Client <SERVER_IP> [<SERVER_PORT>]\" command");
    
    if (args.length > 0) {
      System.out.println("Provided server port is: " + args[0]);
      serverPort = Integer.parseInt(args[0]);
    }
    
    try {
      serverSocket = new ServerSocket(serverPort);
      System.out.println("Socket is opened. Server is listening on port " + serverPort);
    } catch (IOException e) {
      System.out.println("Could not start listening on port: " + serverPort);
      return;
    }
  
    // run thread that processes connections from the clients
    Server server = new Server();
    server.start();
    
    userInput  = new Scanner(System.in);
    
    // the message that server wants to send to other clients
    String message;
    
    System.out.println("Type your message and press <Enter>");
    System.out.println("Type \"exit\" and press <Enter> for exit");
    
    while(keepWorking){
      System.out.print(">> ");
      message = userInput.nextLine();
      
      if (message.equals("exit")) {
        // notify to stop all threads and close all connections
        for (ServerConnection client : clients) {
          client.keepWorking = false;
        }
        keepWorking = false;
      } else if (message.equals("")) {
        // just do nothing
      } else {
        // send received message to every other client
        Server.multicastMessage(message);
      }
    }

    serverSocket.close();
    
    System.out.println("Server was shutted down");
  }

  static void addClient(ServerConnection serverConnection) {
    clients.add(serverConnection);
  }
  
  static void removeClient(ServerConnection serverConnection) {
    clients.remove(serverConnection);
  }
  
  // send messages to all clients except sender of the message
  static void multicastMessage(ServerConnection from, String message) {
    for (ServerConnection client : clients) {
      if (from.hashCode() == client.hashCode()) {
        // don't send message back to the same client
      } else {
        client.sendMessage(message);
      }
      //System.out.print("#");
    }
  }
  
  // send messages to all clients
  static void multicastMessage(String message) {
    for (ServerConnection client : clients) {
      client.sendMessage(message);
    }
  }
}
