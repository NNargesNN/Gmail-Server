package Common;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Gmail implements Serializable {

    private User sender;
    private User receiver;
    private LocalDateTime localDateTime;
    private File attach;
    private String subject;
    private String message;
    private Gmail next;
    private Gmail previous;
    public static final long serialVersionUID = 18L;

    public void setReceiver(User receiver) {

        this.receiver = receiver;
    }


    public Gmail getNext() {

        return next;
    }


    public void setNext(Gmail next) {

        this.next = next;
    }


    public Gmail getPrevious() {

        return previous;
    }


    public void setPrevious(Gmail previous) {

        this.previous = previous;
    }


    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }



    public File getAttach() {
        return attach;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReciever(User reciever) {
        this.receiver = reciever;
    }


    public LocalDateTime getLocalDateTime() {

        return localDateTime;
    }


    public void setLocalDateTime(LocalDateTime localDateTime) {

        this.localDateTime = localDateTime;
    }


    public void setAttach(File attach) {
        this.attach = attach;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
