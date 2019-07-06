package Server;

import Common.User;
import Common.UserMail;

import java.io.*;
import java.util.ArrayList;

import static Server.Server.ALLUSERS;

public class SaveToFile {
    public static void save() throws IOException, ClassNotFoundException {

        for (User user : ALLUSERS) {
            FileOutputStream fos = new FileOutputStream("src/Database/UsersInformation/" + user.getUsername() + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.flush();
            oos.close();
            fos.close();
        }
        for (User user : ALLUSERS) {
          if(user.mails!=null )
            for (int i = 0; i < user.mails.size(); i++) {
                UserMail userMail = user.mails.get(i);
                FileOutputStream fos = new FileOutputStream("src/Database/UsersInformation/" + user.getUsername() + "/email" + (i + 1) + "/" + "email" + (i + 1) + ".ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(userMail);
                oos.flush();
                oos.close();
                fos.close();
            }

        }
    }
}
