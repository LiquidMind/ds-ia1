Name of the image is: liquidmind/openjdk-7-jdk_git_screen_ia1

Sources are in the folder /home/github/ds-ia1/src
Executable classes files are in folder /home/github/ds-ia1/bin

If you want to recompile files you can use command:
javac -d /home/github/ds-ia1/bin /home/github/ds-ia1/src/*.java

Before running applications you have to change working forlder to folder with class files using command:
cd /home/github/ds-ia1/bin

Docker initially was developed to run and watch one single process. And it's good in performing this task. But to test an application we need somehow run at least one instance of Server and multiple instances of Client. There are 4 ways to do so. I will descibe all of them because they may be handy in other assignments or situations.

1. The simplest one is to run all applications (Server and Clients) on one instance of virtual machine. To do this we need to somehow open multiple shells and run applications using them. Easiest way to do so is to use "screen" utility. If it's not installed you should run command "sudo apt-get install screen".

If "screen" is installed after running it you'll see shell to type other command or run applications. To create new window you need to press "Ctrl+A" and then "C" (case of the letter doesn't matter). To move from current screen to previous one you may use "Ctrl+A" twice. To move to the next screen press "Ctrl+A" and then "N". To exit from screen type "exit" in shell.

For my image the command to run screen within virtual machine is:
docker run -i -t liquidmind/openjdk-7-jdk_git_screen_ia1 screen

Now when you know how to open multiple screens you should open Server in one of them and Clients in another. Details of the command will be written below.

Because all applications (Server and Clients) are running in the same container you can use localhost as a server address to connect Clients to.

2. From Docker version 1.3 we can use "docker exec" to enter running docker container. Detailed information about the command is in official documentation at: https://docs.docker.com/reference/commandline/cli/#exec

You should start docker container using run command like this:
docker run -i -t liquidmind/openjdk-7-jdk_git_screen_ia1

In shell you will see text at the beginning of the line like this "root@61f1a8208f81:/#" where 61f1a8208f81 is the container id that we will need in next step.

Enter the same docker container from another instance of boot2docker if you are using Windows (or from another shell session if you use Linux) by running command:
docker exec -i -t 61f1a8208f81 bash

Now you can run Server in one instance and Client in another. You can create as many instances as you want, enter the same container in each of them and run different applications.

Because all applications (Server and Clients) are running in the same container you can use localhost as a server address to connect Clients to.

3. There is application nsenter that can perform almost the same as "docker exec". If you need to enter running containers in old versions of Docker, you can use it. Detailed instruction may be found here: http://jpetazzo.github.io/2014/06/23/docker-ssh-considered-evil/

4. Another way that allow to run your application on completely separated separated containers is to run multiple containers from the same image. You need to open as many instances of boot2docker and execute "docker run" command like this in each of them:
docker run -i -t liquidmind/openjdk-7-jdk_git_screen_ia1

After that you can run Server in one container and Clients in others. Take into consideration that in this case each instance will have its own IP address and you need to look at IP address of each machine (Server shows its IP after start). And use IP address of server machine while running clients.

To test applications you should:
1. If you want to recompile files you can use command:
javac -d /home/github/ds-ia1/bin /home/github/ds-ia1/src/*.java
2. Before running applications you have to change working forlder to folder with class files using command:
cd /home/github/ds-ia1/bin
3. Run "java Server [<SERVER_PORT>]" command. It will run server application on default 2333 port (or <SERVER_PORT> if specified) that will show IP to connect to. If server and client are running on the same machine/container "localhost" may be used instead of the server IP.
4. Run "java Client <SERVER_IP> [<SERVER_PORT>]" command where <SERVER_IP> and <SERVER_PORT> is IP and port of the server from the previous step (or "localhost" without quotes in case of Server and Client located on the same computer)
5. You may run as many clients as you want executing command from step 2 multiple times.
6. You may enter message in any client or at the server. Message will be echoed to each client except those who sent message.
7. To exit server or clients enter "exit" or simply close an application.

P.S. In case if you want to run already compiled applications you should use =java version "1.7.0_79".