package Common;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    public static final long serialVersionUID = 17L;
    private User user;
    private MessageType messageType;
    public UserMail userMail;
    private String sender;
    private String receiver;

    private ArrayList<UserMail> mailArrayList =new ArrayList<>();
    public Message(MessageType messageType , User user , UserMail userMail){
        this.messageType = messageType;
        this.userMail = userMail;
        this.user = user;
    }
    public Message(MessageType messageType, User user) {

        this.user = user;
        this.messageType = messageType;
    }
    public Message(MessageType messageType,ArrayList<UserMail> mailArrayList){
        this.messageType=messageType;
        this.mailArrayList =mailArrayList;
    }

    public Message(MessageType messageType,String sender,String receiver,UserMail userMail){
        this.messageType=messageType;
        this.sender=sender;
        this.receiver=receiver;
        this.userMail=userMail;
    }
    public Message(MessageType messageType, UserMail userMail) {

        this.userMail = userMail;
        this.messageType = messageType;
    }


    public void setUser(User user) {

        this.user = user;
    }

    public Message(MessageType messageType , String sender , String receiver){
        this.messageType = messageType;
        this.sender = sender;
        this.receiver = receiver;
    }
    public void setMessageType(MessageType messageType) {

        this.messageType = messageType;
    }


    public UserMail getUserMail() {

        return userMail;
    }


    public String getSender() {

        return sender;
    }


    public void setSender(String sender) {

        this.sender = sender;
    }


    public String getReceiver() {

        return receiver;
    }


    public void setReceiver(String receiver) {

        this.receiver = receiver;
    }





    public ArrayList<UserMail> getMailArrayList() {

        return mailArrayList;
    }


    public void setMailArrayList(ArrayList<UserMail> mailArrayList) {

        this.mailArrayList = mailArrayList;
    }


    public void setUserMail(UserMail userMail) {

        this.userMail = userMail;
    }


    public User getUser() {

        return user;
    }


    public MessageType getMessageType() {

        return messageType;
    }
}
