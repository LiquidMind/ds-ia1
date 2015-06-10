Sources are in the folder ./src
Executable classes files are in folder ./bin

To test applications you should:
1. Recompile applications using version of JDK instaled on your machine. To compile applications go to ./src folder and run "javac *.java" command.
2. Run "java Server" command. It will run server application that will show IP to connect to. If server and client are running on the same machine "localhost" may be used instead of the server IP.
3. Run "java Client <SERVER_IP>" command where <SERVER_IP> is IP of the server from the previous step (or "localhost" without quotes in case of Server and Client located on the same computer)
4. You may run as many clients as you want exwecuting command from step 2 multiple times.
5. You may enter message in any client or at the server. Message will be echoed to each client except those who sent message.
6. To exit server or clients enter "exit" or simply close an application.

P.S. In case if you want to run already compiled applications you should use java version "1.8.0_25".

