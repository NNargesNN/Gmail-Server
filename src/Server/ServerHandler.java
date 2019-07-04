package Server;

import Common.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static Common.MailType.Inbox;
import static Common.MailType.Sent;
import static Server.Server.ALLUSERS;

public class ServerHandler {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    ServerHandler(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {

        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }


    public ObjectInputStream getInputStream() {

        return inputStream;
    }


    public ObjectOutputStream getOutputStream() {

        return outputStream;
    }


    void handle(Object object) throws IOException, ClassNotFoundException {

        Message message = null;
        User user = null;
        Gmail gmail = null;
        if (object instanceof Message) {
            message = (Message) object;
            if (message.getMessageType().toString().equals("SignUp2")) {
                // Server.initialize();
                user = (User) ((Message) object).getUser();
                Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + user.getUsername()));
                FileOutputStream fos = new FileOutputStream("src/DataBase/UsersInformation/" + user.getUsername() + ".ser");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                objectOutputStream.writeObject(user);
                Logs.register(user);
                Server.ALLUSERS.add(user);
                //   SaveToFile.save();
            } else if (message.getMessageType().toString().equals("SignUp1")) {
                //    Server.initialize();
                User userToCheck = message.getUser();
                boolean canSignUp = true;
                for (User eachUser : Server.ALLUSERS) {
                    if (eachUser.equals(userToCheck)) {
                        canSignUp = false;
                        this.getOutputStream().writeObject(MessageType.NoSignUp);
                        System.out.println("No Sign UP");
                        this.getOutputStream().flush();
                    }
                }
                if (canSignUp) {
                    this.getOutputStream().writeObject(MessageType.canSignUp);
                    System.out.println("Sign Up");
                    this.getOutputStream().flush();
                }

            } else if (message.getMessageType().toString().equals("SignIn")) {
                //  Server.initialize();
                user = message.getUser();
                boolean canSignIn = false;
                for (User eachUser : Server.ALLUSERS) {
                    if (eachUser.getUsername().equals(user.getUsername()) && eachUser.getPassword().equals(user.getPassword())) {
                        canSignIn = true;
                        this.getOutputStream().writeObject(new Message(MessageType.canSignUp, eachUser));
                        Logs.login(eachUser);
                        this.getOutputStream().flush();
                    }
                }
                if (!canSignIn) {
                    this.getOutputStream().writeObject(MessageType.NoSignIn);
                    this.getOutputStream().flush();
                }

            } else if (message.getMessageType().toString().equals("Change")) {
                // Server.initialize();
                user = message.getUser();
                File path = new File("src/DataBase/UsersInformation");
                File[] listOfFiles = path.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    File file = listOfFiles[i];
                    if (file.getName().startsWith(user.getUsername()) && file.getName().endsWith(".ser")) {
                        file.delete();
                        FileOutputStream fos = new FileOutputStream("src/DataBase/UsersInformation/" + user.getUsername() + ".ser");
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                        objectOutputStream.writeObject(user);
                    }
                }
                for (User eachUser : ALLUSERS) {
                    if (eachUser.getUsername().equals(user.getUsername())) {
                        System.out.println(eachUser.getUsername() + " changed Information.\n");
                        eachUser.setPhoneNumber(user.getPhoneNumber());
                        eachUser.setLastName(user.getLastName());
                        eachUser.setName(user.getName());
                        eachUser.setPassword(user.getPassword());
                        eachUser.setMails(user.getMails());
                        eachUser.setAge(user.getAge());
                        eachUser.setGender(user.getGender());
//                        eachUser.setImage(user.getImage());
                        break;
                    }
                }
//                SaveToFile.save();
            } else if (message.getMessageType().toString().equals("Refresh")) {
                //   Server.initialize();
                user = message.getUser();
                for (User eachUser : Server.ALLUSERS) {
                    if (eachUser.getUsername().equals(user.getUsername()) /*&& eachUser.getPassword().equals(user.getPassword())*/) {
                        System.out.println(eachUser.getMails().size());
                        this.getOutputStream().writeObject(new Message(MessageType.Refresh, eachUser));
                        System.out.println(eachUser.getUsername() + " refresh page\n");
                        this.getOutputStream().flush();
                    }
                }

            } else if (message.getMessageType().toString().equals("Compose")) {
                //     Server.initialize();
                boolean sent = false;
                gmail = message.getUserMail().getGmail();
                Set<MailType> mailTypeSet;
                for (User eachUser : Server.ALLUSERS) {
                    mailTypeSet = new HashSet<>();
                    if (eachUser.getUsername().equals(gmail.getReceiver().getUsername())) {
                        System.out.println("add mail to receiver mails");
                        sent = true;
//                        if (!eachUser.mails.contains(new UserMail(gmail, null))) {
                        mailTypeSet.add(Inbox);
                        eachUser.mails.add(new UserMail(gmail, mailTypeSet));
                        ////////////////
                        user = message.getUserMail().getGmail().getReceiver();
                        // Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + user.getUsername()));
                        File Dir = new File("src/DataBase/UsersMails/" + user.getUsername());
                        int numberOfFiles;
                        if (Dir.listFiles() == null || Objects.requireNonNull(Dir.listFiles()).length == 0)
                            numberOfFiles = 0;
                        else
                            numberOfFiles = Objects.requireNonNull(Dir.listFiles()).length;
                        Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + user.getUsername() + "/" + "email" + (numberOfFiles + 1)));
                        FileOutputStream fos = new FileOutputStream("src/DataBase/UsersMails/" + user.getUsername() + "/" + "email" + (numberOfFiles + 1) + "/" + "email" + (numberOfFiles + 1) + ".ser");
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                        objectOutputStream.writeObject(new UserMail(gmail, mailTypeSet));
                        objectOutputStream.close();
                        //////////////
                        // }
                    }
                    if (!sent) {
                        System.out.println("not found :( ");
                        Gmail gmail1 = new Gmail();
                        gmail1.setSubject("404 NotFound");
                        gmail1.setSender(new User("mailerdaemon@googlemail.com"));
                        gmail1.setMessage(" Receiver Of  mail was not found  :(    ");
                        gmail1.setReceiver(message.getUserMail().getGmail().getSender());
                        Set<MailType> inbox = new HashSet<>();
                        inbox.add(Inbox);
                        UserMail userMail = new UserMail(gmail1, inbox);
                        userMail.setPrevious(message.getUserMail());
                        message.userMail.setNext(userMail);
                        message.userMail.getGmail().getSender().mails.add(userMail);
                        System.out.println(userMail.getPrevious() + "   " + userMail.getPrevious().getGmail().getSubject() + "  " +
                                message.userMail.getNext().getGmail().getMessage());
                        System.out.println("\n");
                        File Dir = new File("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername());
                        int numberOfFiles;
                        if (Dir.listFiles() == null || Objects.requireNonNull(Dir.listFiles()).length == 0)
                            numberOfFiles = 0;
                        else
                            numberOfFiles = Objects.requireNonNull(Dir.listFiles()).length;
                        Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername() + "/" + "email" + (numberOfFiles + 1)));
                        FileOutputStream fos = new FileOutputStream("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername() + "/" + "email" + (numberOfFiles + 1) + "/" + "email" + (numberOfFiles + 1) + ".ser");
                        //save attach in that folder
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                        objectOutputStream.writeObject(userMail);
                        objectOutputStream.close();

                    }
                    mailTypeSet = new HashSet<>();
                    if (sent  && eachUser.getUsername().equals(gmail.getSender().getUsername())) {
                        System.out.println("add mail to sender mails");
//                        if (!eachUser.mails.contains(new UserMail(gmail, null))) {
                        mailTypeSet.add(Sent);
                        eachUser.mails.add(new UserMail(gmail, mailTypeSet));
                        ////////////////
                        user = message.getUserMail().getGmail().getSender();
                        // Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + user.getUsername()));
                        File Dir = new File("src/DataBase/UsersMails/" + user.getUsername());
                        int numberOfFiles;
                        if (Dir.listFiles() == null || Objects.requireNonNull(Dir.listFiles()).length == 0)
                            numberOfFiles = 0;
                        else
                            numberOfFiles = Objects.requireNonNull(Dir.listFiles()).length;
                        Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername() + "/" + "email" + (numberOfFiles + 1)));
                        FileOutputStream fos = new FileOutputStream("src/DataBase/UsersMails/" + user.getUsername() + "/" + "email" + (numberOfFiles + 1) + "/" + "email" + (numberOfFiles + 1) + ".ser");
                        //save attach in that folder
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                        objectOutputStream.writeObject(new UserMail(gmail, mailTypeSet));
                        //check?
                        objectOutputStream.close();
                        //////////////
                        // }
                    }
                }
                //   SaveToFile.save();
            }

        }
    }
}
