package Server;

import Common.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AllPermission;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static Common.MailType.*;
import static Server.Server.ALLUSERS;

public class ServerHandler {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private File dir;


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

                user = (User) ((Message) object).getUser();
                Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + user.getUsername()));
                FileOutputStream fos = new FileOutputStream("src/DataBase/UsersInformation/" + user.getUsername() + ".ser");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                objectOutputStream.writeObject(user);
                objectOutputStream.flush();
                this.getOutputStream().writeObject(user);
                this.getOutputStream().flush();
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(user.getImage());
//                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
//                FileOutputStream fileOutputStream = new FileOutputStream("src/DataBase/SignUppedUsers/" + user.getUsername() + ".jpg");
//                ImageIO.write(bufferedImage, "jpg", fileOutputStream);
//                fileOutputStream.close();
//                byteArrayInputStream.close();
                Logs.register(user);
                Server.ALLUSERS.add(user);
                //    SaveToFile.save();
            } else if (message.getMessageType().toString().equals("SignUp1")) {
                // Server.initialize();
                User userToCheck = message.getUser();
                boolean canSignUp = true;
                for (User eachUser : Server.ALLUSERS) {
                    if (eachUser.getUsername().equals(userToCheck.getUsername())) {
                        canSignUp = false;
                        this.getOutputStream().writeObject(MessageType.NoSignUp);
                        System.out.println("No Sign UP  " + message.getUser().getUsername());
                        this.getOutputStream().flush();
                        break;
                    }
                }
                if (canSignUp) {
                    this.getOutputStream().writeObject(MessageType.canSignUp);
                    System.out.println("Sign Up   " + message.getUser().getUsername());
                    this.getOutputStream().flush();
                }

            } else if (message.getMessageType().toString().equals("SignIn")) {

                user = message.getUser();
                System.out.println("user wants to signin : " + user.getUsername());
                boolean canSignIn = false;
                for (User eachUser : Server.ALLUSERS) {
                    if (eachUser.getUsername().equals(user.getUsername()) && eachUser.getPassword().equals(user.getPassword())) {
                        canSignIn = true;
                        System.out.println("found ::  " + eachUser.getUsername());
                        this.getOutputStream().writeObject(new Message(MessageType.canSignIn, eachUser));
                        this.getOutputStream().flush();
                        Logs.login(eachUser);
                        break;
                    }
                }
                if (!canSignIn) {
                    this.getOutputStream().writeObject(MessageType.NoSignIn);
                    this.getOutputStream().flush();
                }

            } else if (message.getMessageType().toString().equals("Change")) {

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
                        objectOutputStream.flush();
                    }
                }
                for (User eachUser : ALLUSERS) {
                    if (eachUser.getUsername().equals(user.getUsername())) {
                        System.out.println(eachUser.getUsername() + " changed Information.\n");
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setPhoneNumber(user.getPhoneNumber());
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setLastName(user.getLastName());
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setName(user.getName());
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setPassword(user.getPassword());
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setMails(user.getMails());
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setAge(user.getAge());
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setGender(user.getGender());
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setMails(user.getMails());
                        //      ALLUSERS.get(ALLUSERS.indexOf(eachUser)).setImage(user.getImage());
                        break;
                    }
                }


            } else if (message.getMessageType().toString().equals("Refresh")) {

                user = message.getUser();
                for (User eachUser : Server.ALLUSERS) {
                    if (eachUser.getUsername().equals(user.getUsername()) /*&& eachUser.getPassword().equals(user.getPassword())*/) {
                        System.out.println(eachUser.getMails().size());
                        this.getOutputStream().writeObject(new Message(MessageType.Refresh, eachUser.mails));
                        System.out.println(eachUser.getUsername() + " refresh page\n");
                        this.getOutputStream().flush();
                    }
                }

            } else if (message.getMessageType().toString().equals("getMails")) {
                User u = message.getUser();

                for (User eachUser : ALLUSERS) {
                    if (eachUser.getUsername().equals(u.getUsername())) {
                        ArrayList<UserMail> mails = eachUser.getMails();
                        this.getOutputStream().writeObject(new Message(MessageType.receiveMails, mails));
                        this.getOutputStream().flush();
                        System.out.println("mails sent getMails");
                        break;
                    }
                }

            } else if (message.getMessageType().toString().equals("deleteMail")) {
                Gmail m = message.userMail.getGmail();
                User u = message.getUser();
                for (User eachUser : ALLUSERS) {
                    if (eachUser.getUsername().equals(u.getUsername())) {
                        for (int i = 0; i < ALLUSERS.get(ALLUSERS.indexOf(u)).mails.size(); i++) {
                           if( ALLUSERS.get(ALLUSERS.indexOf(u)).mails.get(i).getGmail().equals(m)){
                               ALLUSERS.get(ALLUSERS.indexOf(u)).mails.get(i).mailTypeSet.add(MailType.deleteMail);
                           };
                        }
                        break;
                    }
                }
                Logs.deleteMsg(u,m);

            } else if (message.getMessageType().toString().equals("Inbox")) {
                User u = message.getUser();

                for (User eachUser : ALLUSERS) {
                    if (eachUser.getUsername().equals(u.getUsername())) {
                        ArrayList<UserMail> mails = (ArrayList<UserMail>) eachUser.getMails().stream().filter(a -> a.getMailTypeSet().contains(Inbox)&& !a.mailTypeSet.contains(deleteMail)).collect(Collectors.toList());
                        this.getOutputStream().writeObject(new Message(MessageType.Inbox, mails));
                        this.getOutputStream().flush();

                        break;
                    }
                }

            } else if (message.getMessageType().toString().equals("Sent")) {
                User u = message.getUser();

                for (User eachUser : ALLUSERS) {
                    if (eachUser.getUsername().equals(u.getUsername())) {
                        ArrayList<UserMail> mails = (ArrayList<UserMail>) eachUser.getMails().stream().filter(a -> a.getMailTypeSet().contains(Sent)&&!a.mailTypeSet.contains(deleteMail)).collect(Collectors.toList());
                        this.getOutputStream().writeObject(new Message(MessageType.Sent, mails));
                        this.getOutputStream().flush();

                        break;
                    }
                }

            } else if (message.getMessageType().toString().equals("Replied")) {
                gmail = message.getUserMail().getGmail();
                Set<MailType> mailTypeSet;
                for (User eachUser : Server.ALLUSERS) {
                    mailTypeSet = new HashSet<>();
                    if (eachUser.getUsername().equals(gmail.getReceiver().getUsername())) {

                        mailTypeSet.add(Inbox);
                        mailTypeSet.add(Replied);

                        UserMail mail = new UserMail(gmail, mailTypeSet);
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).mails.add(mail);

                        UserMail prev = message.getUserMail().getPrevious();
                        user = message.getUserMail().getGmail().getReceiver();
                        User u = ALLUSERS.get(ALLUSERS.indexOf(user));
                        System.out.println(u.mails.indexOf(prev));
                        ALLUSERS.get(ALLUSERS.indexOf(user)).mails.get(u.mails.indexOf(new UserMail(prev.getGmail(), null))).setNext(mail);
                        mail.setPrevious(ALLUSERS.get(ALLUSERS.indexOf(user)).mails.get(ALLUSERS.get(ALLUSERS.indexOf(user)).mails.indexOf(prev)));
                        ALLUSERS.get(ALLUSERS.indexOf(user)).mails.add(mail);
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
                        objectOutputStream.flush();
                        objectOutputStream.close();

                    }

                }
                for (User eachUser : ALLUSERS) {
                    mailTypeSet = new HashSet<>();
                    if (eachUser.getUsername().equals(message.getUserMail().getGmail().getSender().getUsername())) {

                        mailTypeSet.add(Sent);
                        mailTypeSet.add(Replied);
                        UserMail mail = new UserMail(gmail, mailTypeSet);

                        ALLUSERS.get(ALLUSERS.indexOf(message.userMail.getGmail().getSender())).mails.add(mail);

                        user = message.getUserMail().getGmail().getSender();
                        UserMail prev = message.getUserMail().getPrevious();
                        ALLUSERS.get(ALLUSERS.indexOf(user)).mails.get(ALLUSERS.get(ALLUSERS.indexOf(user)).mails.indexOf(prev)).setNext(mail);
                        mail.setPrevious(ALLUSERS.get(ALLUSERS.indexOf(user)).mails.get(ALLUSERS.get(ALLUSERS.indexOf(user)).mails.indexOf(prev)));
                        ALLUSERS.get(ALLUSERS.indexOf(user)).mails.add(mail);
                        // Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + user.getUsername()));
                        File dir = new File("src/DataBase/UsersMails/" + user.getUsername());
                        int numberOfFiles;
                        if (dir.listFiles() == null || (dir.listFiles()).length == 0)
                            numberOfFiles = 0;
                        else
                            numberOfFiles = (dir.listFiles()).length;
                        Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername() + "/" + "email" + (numberOfFiles + 1)));
                        FileOutputStream fos = new FileOutputStream("src/DataBase/UsersMails/" + user.getUsername() + "/" + "email" + (numberOfFiles + 1) + "/" + "email" + (numberOfFiles + 1) + ".ser");
                        //save attach in that folder
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                        objectOutputStream.writeObject(new UserMail(message.getUserMail().getGmail(), mailTypeSet));
                        objectOutputStream.flush();

                        objectOutputStream.close();
                        fos.close();

                    }
                }
                Logs.reply(gmail);


            } else if (message.getMessageType().toString().equals("SignOut")) {
                User u = message.getUser();
                ArrayList<UserMail> mails = message.getMailArrayList();
                for (int i = 0; i < mails.size(); i++) {
                    for (int j = 0; j < ALLUSERS.get(ALLUSERS.indexOf(u)).mails.size(); j++) {
                        if (ALLUSERS.get(ALLUSERS.indexOf(u)).mails.get(j).equals(mails.get(i))) {
                            ALLUSERS.get(ALLUSERS.indexOf(u)).mails.get(j).getGmail().setImportant(mails.get(i).getGmail().isImportant());
                            ALLUSERS.get(ALLUSERS.indexOf(u)).mails.get(j).getGmail().setRead(mails.get(i).getGmail().isRead());
                        }
                    }

                }

            } else if (message.getMessageType().toString().equals("Compose")) {

                boolean sent = false;
                gmail = message.getUserMail().getGmail();
                Set<MailType> mailTypeSet;
                for (User eachUser : Server.ALLUSERS) {
                    mailTypeSet = new HashSet<>();
                    if (eachUser.getUsername().equals(gmail.getReceiver().getUsername())) {
                      //  System.out.println("add mail to receiver mails");
                        sent = true;
//                        if (!eachUser.mails.contains(new UserMail(gmail, null))) {
                        mailTypeSet.add(Inbox);
                        // eachUser.mails.add(new UserMail(gmail, mailTypeSet));
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).mails.add(new UserMail(gmail, mailTypeSet));

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
                        objectOutputStream.flush();
                        objectOutputStream.close();
                        Logs.send(gmail);
                        //////////////
                        // }
                    }

                }
                for (User eachUser : ALLUSERS) {
                    mailTypeSet = new HashSet<>();
                    if (eachUser.getUsername().equals(message.getUserMail().getGmail().getSender().getUsername())) {
                        System.out.println("add mail to sender mails");
//                        if (!eachUser.mails.contains(new UserMail(gmail, null))) {
                        if(sent)
                        mailTypeSet.add(Sent);
                        else mailTypeSet.add(Inbox);
                        UserMail u = new UserMail(message.getUserMail().getGmail(), mailTypeSet);
                        // eachUser.mails.add(new UserMail(message.getUserMail().getGmail(), mailTypeSet));
                        ALLUSERS.get(ALLUSERS.indexOf(eachUser)).mails.add(u);

                        user = message.getUserMail().getGmail().getSender();
                        // Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + user.getUsername()));
                        File dir = new File("src/DataBase/UsersMails/" + user.getUsername());
                        int numberOfFiles;
                        if (dir.listFiles() == null || (dir.listFiles()).length == 0)
                            numberOfFiles = 0;
                        else
                            numberOfFiles = (dir.listFiles()).length;
                        Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername() + "/" + "email" + (numberOfFiles + 1)));
                        FileOutputStream fos = new FileOutputStream("src/DataBase/UsersMails/" + user.getUsername() + "/" + "email" + (numberOfFiles + 1) + "/" + "email" + (numberOfFiles + 1) + ".ser");
                        //save attach in that folder
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                        objectOutputStream.writeObject(new UserMail(message.getUserMail().getGmail(), mailTypeSet));
                        objectOutputStream.flush();

                        objectOutputStream.close();
                        fos.close();

                    }
                }
                if (!sent) {
                //    System.out.println("not found :( ");
                    Gmail gmail1 = new Gmail();
                    gmail1.setSubject("404 NotFound");
                    gmail1.setSender(new User("mailerdaemon@googlemail.com"));
                    gmail1.setMessage(" Receiver Of  mail was not found  :(    ");
                    gmail1.setReceiver(message.getUserMail().getGmail().getSender());
                    gmail1.setLocalDateTime(LocalDateTime.now());
                    Set<MailType> inbox = new HashSet<>();
                    inbox.add(Inbox);
                    UserMail userMail = new UserMail(gmail1, inbox);
                    ALLUSERS.get(ALLUSERS.indexOf(message.userMail.getGmail().getSender())).mails.add(userMail);
                    userMail.setPrevious(ALLUSERS.get(ALLUSERS.indexOf(message.userMail.getGmail().getSender())).mails.get(ALLUSERS.get(ALLUSERS.indexOf(message.userMail.getGmail().getSender())).mails.indexOf(message.userMail)));
                    ALLUSERS.get(ALLUSERS.indexOf(message.userMail.getGmail().getSender())).mails.get(ALLUSERS.get(ALLUSERS.indexOf(message.userMail.getGmail().getSender())).mails.indexOf(message.userMail)).setNext(userMail);
//                message.userMail.getGmail().getSender().mails.add(userMail);
                    File Dir = new File("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername());
                    int numberOfFiles;
                    if (Dir.listFiles() == null || (Dir.listFiles()).length == 0)
                        numberOfFiles = 0;
                    else
                        numberOfFiles = (Dir.listFiles()).length;
                    Files.createDirectories(Paths.get("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername() + "/" + "email" + (numberOfFiles + 1)));
                    FileOutputStream fos = new FileOutputStream("src/DataBase/UsersMails/" + message.getUserMail().getGmail().getSender().getUsername() + "/" + "email" + (numberOfFiles + 1) + "/" + "email" + (numberOfFiles + 1) + ".ser");
                    //save attach in that folder
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
                    objectOutputStream.writeObject(userMail);
                    objectOutputStream.flush();
                    objectOutputStream.close();

                }

            }

        }
    }
}
