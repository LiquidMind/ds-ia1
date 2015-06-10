import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;


public class Client {
  //opened client socket
  static Socket clientSocket = null;
  //default port of the server to connect to
  static String serverIP = null;
  // default port of the server to connect to
  static int serverPort = 2333;
  //flag to notify that client should keep working
  static boolean keepWorking = true;
  // scanner to read user input
  private static Scanner userInput = null;
  // socket output and input streams
  public static DataOutputStream out = null; 
  public static BufferedReader in = null;
 
  public static void main(String[] args) throws IOException {
    // the message that user wants to send to other clients
    String message = "";
    
    if (args.length > 0) {
      System.out.println("Provided server IP is: " + args[0]);
      serverIP = args[0];
    } else {
      System.out.println("Use \"Client <SERVER_IP> [<SERVER_PORT>]\" command to run client");
      return;
    }
    
    if (args.length > 1) {
      System.out.println("Provided server port is: " + args[1]);
      serverPort = Integer.parseInt(args[1]);
    }

    userInput  = new Scanner(System.in);

    try {
      // make a connection to the server
      clientSocket = new Socket(serverIP, serverPort);
    } catch (ConnectException e) {
      System.out.println("Couldn't connect to the server with IP " + serverIP + " on port " + serverPort);
      return;
    }
    
    // get socket output and input streams
    out = new DataOutputStream(clientSocket.getOutputStream());
    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    ClientConnection clientConnection = new ClientConnection(clientSocket);
    clientConnection.start();

    System.out.println("Client is connected to the server " + serverIP + " on port " + serverPort);
    System.out.println("Type your message and press <Enter>");
    System.out.println("Type \"exit\" and press <Enter> for exit");
    
    while(keepWorking){
      System.out.print(">> ");
      if (userInput.hasNextLine()) {
        message = userInput.nextLine();
      } else {
        message = "exit";
      }
      
      if (message.equals("exit")) {
        // notify to stop thread and close connection
        clientConnection.keepWorking = false;
        keepWorking = false;
      } else if (message.equals("")) {
        // just do nothing
      } else {
        try {
          out.writeBytes(message + '\n');
        } catch (IOException e) {
          // probably server was shutted down or link is broken
          // notify to stop thread and close connection
          clientConnection.keepWorking = false;
          System.out.println("Server was disconnected");
          //keepWorking = false;
        }
      }   
    }
    
    //clientSocket.close();
    
    System.out.println("Client was shutted down");
  }

}
