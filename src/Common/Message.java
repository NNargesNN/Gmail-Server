package Common;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    public static final long serialVersionUID = 17L;
    private User user;
    private MessageType messageType;

    //


    public UserMail getUserMail() {

        return userMail;
    }


    public void setUserMail(UserMail userMail) {

        this.userMail = userMail;
    }


    public UserMail userMail;


    public Message(MessageType messageType, User user) {

        this.user = user;
        this.messageType = messageType;
    }




    public Message(MessageType messageType, UserMail userMail) {

        this.userMail = userMail;
        this.messageType = messageType;
    }


    public void setUser(User user) {

        this.user = user;
    }


    public void setMessageType(MessageType messageType) {

        this.messageType = messageType;
    }


    public User getUser() {

        return user;
    }


    public MessageType getMessageType() {

        return messageType;
    }
}
