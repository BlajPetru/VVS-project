package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class UI implements Runnable{
    static int port;
    static boolean runServer=true;
    static String  statusServer;
    static Server server;

    public static String getStatusServer() {
        return statusServer;
    }

    public static boolean getConnectionOk() {
        return connectionOk;
    }

    public static void setConnectionOk(boolean connectionOk) {
        UI.connectionOk = connectionOk;
    }

    static boolean connectionOk=false;

    public static Server getServer() {
        return server;
    }

    public static void setServer(Server server) {
        UI.server = server;
    }

    public static void main(String[] args) {
        server = new Server();
        while (!connectionOk) {
            setPortInterface();
        }

        Thread interfaceThread = new Thread(new UI());
        interfaceThread.start();

        try {
            readComands();
        } catch (IOException e) {
            System.out.println("\n Invalid command !");
        }

    }

    public static void readComands() throws IOException {
        while(true)
        {
            System.out.print("Please enter a new command:");
            BufferedReader readerCommand =  new BufferedReader(new InputStreamReader(System.in));
            String commandLine = readerCommand.readLine();
            verifCommand(commandLine);
        }
    }

    public static void verifCommand(String command) throws IOException {
        if (command.equals("status")) {
            System.out.println("Status server:"+statusServer);
        }else if (command.equals("systeminfo")) {
            System.out.println("Status server : "+statusServer);
            System.out.println("Port : "+port);
            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("Host : "+IP.getHostAddress());
        }else if (command.equals("start")) {
            server.setStateServer(1);
            statusServer="RUNNING";
            System.out.println("Server started at the port:"+port);
        }else if (command.equals("maintenance")){
            server.setStateServer(2);
            statusServer="MAINTENANCE";
            System.out.println("The server is now in maintenance");
        }else if (command.equals("stop")){
            server.setStateServer(3);
            statusServer="STOP";
            System.out.println("The server is stop");
        }else{
            statusServer="WAITING";
            System.out.println("Invalid command");
        }
    }

    public static void setPortInterface() {
        System.out.print("Please enter the port you want the server to start: ");
        BufferedReader readerCommand =  new BufferedReader(new InputStreamReader(System.in));
        try {
            port = Integer.parseInt(readerCommand.readLine());
        } catch (NumberFormatException | IOException e) {
            System.out.println("Invalid port 1");
            port=-1;
        }
        if(server.setPort(port))
        {
            if(server.acceptServerPort())
            {
                System.out.println("The server started at port " + port + "\n"+ "To start it please write 'start' \n");
                connectionOk=true;
                statusServer="STOP";
            }
        }
    }

    @Override
    public void run() {
        while(runServer) {
            server.listenForClients();
        }
    }
}
