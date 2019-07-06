package Server;



import Common.Gmail;
import Common.MailType;
import Common.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logs {
    public static void connect(User user){
        System.out.println(user.getUsername()+" connect");
        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }
    public static void login(User user){
        System.out.println(user.getUsername()+" signin");
        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }

    public static void register (User user){
        System.out.println(user.getUsername()+" register ");
        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }
    public static void receive(Gmail mail){
        System.out.println(mail.getReceiver().getUsername()+" receiver");
   //     System.out.println("message: "+mail.getSender() + mail.getFile().getAbsolutePath());
        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }

    public static void send(Gmail mail){
        System.out.println(mail.getSender().getUsername()+" send");
    //    System.out.println("message: "+mail.getSubject() + mail.getFile().getAbsolutePath()+ " to "+ mail.getReceiver());

        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }

    public static void reply(Gmail mail){
        System.out.println(mail.getSender().getUsername()+" reply");
   //     System.out.println("message: "+mail.getSubject() + mail.getFile().getAbsolutePath()+ " to "+ mail.getReceiver());

        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }
    public static void forward(Gmail mail){
        System.out.println(mail.getSender().getUsername()+" forward");
      //  System.out.println("message: "+mail.getSubject() + mail.getFile().getAbsolutePath()+ " from "+ mail.getSender()+" to "+mail.getReceiver());

        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }

    public static void mark(User user, Gmail mail, MailType mailType){
        System.out.println(user.getUsername()+" mark");
        System.out.println("message: "+mail.getSubject()+" "+mail.getSender()+" as "+mailType);
        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }
    public static void deleteMsg(User user, Gmail mail){
        System.out.println(user.getUsername()+" removemsg");
        System.out.println("message: "+mail.getSubject()+" "+mail.getSender());
        System.out.print("time: ");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(dateTime.format(formatter));
        System.out.println();
    }

}
