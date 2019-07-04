package Server;

import Common.User;
import Common.UserMail;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server implements Runnable {
    public static final int requestPort = 1379;
    public static final String serverIP = "localhost";
    private static ServerSocket requestServerSocket;
    public static List<User> ALLUSERS = new ArrayList<>();


    public static void initialize() throws IOException, ClassNotFoundException {
        ALLUSERS = new ArrayList<>();


        File path = new File("src/DataBase/UsersInformation");
        File[] listOfFiles = path.listFiles();
        for (int i = 0; i <listOfFiles.length ; i++) {
            File file = listOfFiles[i];
            if(file.isFile() && file.getName().endsWith(".ser")){
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                ALLUSERS.add((User) ois.readObject());
            }
        }
        System.out.println(ALLUSERS.size());
        //////////////////
        File Path = new File("src/DataBase/UsersMails");
        File[] listOfUsers = Path.listFiles();
        int n1=(listOfUsers==null || listOfUsers.length==0)?0:listOfUsers.length;
        for (int i = 0; i <n1 ; i++) {
            File file = listOfUsers[i];

            for (int j = 0; j <file.listFiles().length ; j++) {
                File[] inner=file.listFiles();
                int n2=(inner==null || inner.length==0)?0:inner.length;
                for (int k = 0; k <n2; k++) {
                    File[] files=inner[k].listFiles();
                    int n3=(files==null || files.length==0)?0:files.length;
                    for (int y = 0; y <n3 ; y++) {
                        if (files[y].isFile() && files[y].getName().endsWith(".ser")) {
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files[y]));
                            for (int l = 0; l < ALLUSERS.size(); l++) {
                                if (ALLUSERS.get(l).getUsername().equals(file.getName()/*.substring(0, file.getName().length() - 4)*/)) {
                                    ALLUSERS.get(l).mails.add((UserMail) ois.readObject());
                                }
                            }
                            ois.close();
                        }
                    }
                }

            }
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server.initialize();
        Server.start();
    }

    public static void start() {
        try {
            requestServerSocket = new ServerSocket(requestPort);
            Thread serverThread = new Thread(new Server(), "Server Thread");
            serverThread.start();
        } catch (IOException e) {
            // ignore it
        }
    }

    @Override
    public void run() {
        while (!requestServerSocket.isClosed()) {
            try {
                new Thread(new ServerRunner(requestServerSocket.accept()), "Server Runner").start();
                System.out.println("connected");
            } catch (IOException e) {
                // ignore it
            }
        }
    }
}

class ServerRunner implements Runnable {
    private Socket serverSocket;
    private ServerHandler serverHandler;

    public ServerRunner(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {


        try {
            serverHandler = new ServerHandler(serverSocket,
                    new ObjectInputStream(serverSocket.getInputStream()),
                    new ObjectOutputStream(serverSocket.getOutputStream()));

            while (true) {
                Object object = serverHandler.getInputStream().readObject();
                serverHandler.handle(object);
            }
        } catch (IOException | ClassNotFoundException e) {
            /* Ignore it */
        }
    }

}

