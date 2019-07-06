
import Common.*;
import Server.Server;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.getDefaultUncaughtExceptionHandler;
import static org.junit.Assert.*;
import static java.lang.Thread.sleep;
//import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

public class ServerTest {
    @Test
    public void test() throws InterruptedException {
        Server.start();
        try {
            Socket client = new Socket(Server.serverIP, Server.requestPort);
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            User user = new User("SomeOne");
            user.setPassword("123456789");
            out.writeObject(new Message(MessageType.SignUp1, user));
            out.flush();
            sleep(1000);
        MessageType result=(MessageType) in.readObject();
            assertTrue(result.toString().equals("canSignUp"));
            sleep(1000);
            assertFalse(Server.ALLUSERS.contains(user));
            out.writeObject(new Message(MessageType.SignUp2, user));
            out.flush();
            sleep(1000);
            Object m = in.readObject();
            assertTrue(m instanceof User);

            sleep(1000);
            out.writeObject(new Message(MessageType.SignIn, user));
            out.flush();
            sleep(1000);
            Object o = in.readObject();
            assertTrue(o instanceof Message);
            assertEquals("canSignIn", ((Message) o).getMessageType().toString());
            User user2 = new User("NoOne");
            user2.setPassword("123456");
            out.writeObject(new Message(MessageType.SignIn, user2));
            out.flush();
            sleep(1000);
            Object o2 = in.readObject();
            assertTrue(o2 instanceof MessageType);
            assertEquals("NoSignIn", ((MessageType) o2).toString());
            //--------------------
            User user3 = new User("SomeOne2");
            user.setPassword("123456789");
            out.writeObject(new Message(MessageType.SignUp1, user3));
            out.flush();
            sleep(1000);
            MessageType result2=(MessageType) in.readObject();
            assertTrue(result2.toString().equals("canSignUp"));
            sleep(1000);
            assertFalse(Server.ALLUSERS.contains(user));
            out.writeObject(new Message(MessageType.SignUp2, user3));
            out.flush();
            sleep(1000);
            Object m2 = in.readObject();
            assertTrue(m2 instanceof User);

            sleep(1000);
            out.writeObject(new Message(MessageType.SignIn, user3));
            out.flush();
            sleep(1000);
            Object o4 = in.readObject();
            assertTrue(o4 instanceof Message);
            assertEquals("canSignIn", ((Message) o4).getMessageType().toString());


        }catch (IOException|ClassNotFoundException e){

        }
    }

    @Test
    public void testMail() throws InterruptedException {
        Server.start();
        try {
            Socket client = new Socket(Server.serverIP, Server.requestPort);
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            User user = new User("SomeOne");
            user.setPassword("123456789");
            out.writeObject(new Message(MessageType.SignIn, user));
            out.flush();
            sleep(1000);
            Object o = in.readObject();
            assertTrue(o instanceof Message);
            assertEquals("canSignIn", ((Message) o).getMessageType().toString());
            Gmail gmail = new Gmail();
            gmail.setSender(user);
            gmail.setReceiver(new User("Nothing"));
            gmail.setSubject("subject");
            gmail.setMessage("text");
            gmail.setLocalDateTime(LocalDateTime.now());
            Set<MailType> mailTypeSet = new HashSet<>();
            mailTypeSet.add(MailType.OutBox);
            UserMail mail = new UserMail(gmail, mailTypeSet);
            user.mails.add(mail);
            out.writeObject(new Message(MessageType.Compose, mail));
            out.flush();
            sleep(2000);
            assertTrue(Server.ALLUSERS.get(Server.ALLUSERS.indexOf(user)).mails.size() == 2);

        }catch (Exception r){}
    } public void composetest() throws InterruptedException {
        Server.start();
        try {
            Socket client = new Socket(Server.serverIP, Server.requestPort);
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            User user = new User("SomeOne");
            user.setPassword("123456789");
            out.writeObject(new Message(MessageType.SignIn, user));
            out.flush();
            sleep(1000);
            Object o = in.readObject();
            assertTrue(o instanceof Message);
            assertEquals("canSignIn", ((Message) o).getMessageType().toString());
            Gmail gmail = new Gmail();
            gmail.setSender(user);
            gmail.setReceiver(new User("SomeOne2"));
            gmail.setSubject("subject2");
            gmail.setMessage("text2");
            gmail.setLocalDateTime(LocalDateTime.now());
            Set<MailType> mailTypeSet = new HashSet<>();
            mailTypeSet.add(MailType.OutBox);
            UserMail mail = new UserMail(gmail, mailTypeSet);
            user.mails.add(mail);
            out.writeObject(new Message(MessageType.Compose, mail));
            out.flush();
            sleep(2000);
            User user1=new User("User3");
            assertTrue(Server.ALLUSERS.get(Server.ALLUSERS.indexOf(user1)).mails.size()==1);

        }catch (Exception r){}
    }
}