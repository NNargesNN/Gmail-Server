package Common;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Gmail implements Serializable {
    private User sender;
    private User receiver;
    public static final long serialVersionUID = 18L;
    private LocalDateTime localDateTime;
    private File attach;
    private String subject;
    private String message;
    private Gmail next;
    private Gmail previous;
    private boolean important;
    private boolean read=false;
    public LocalDateTime getLocalDateTime() {

        return localDateTime;
    }


    public void setLocalDateTime(LocalDateTime localDateTime) {

        this.localDateTime = localDateTime;
    }


    public boolean isImportant() {

        return important;
    }


    public void setImportant(boolean important) {

        this.important = important;
    }


    public boolean isRead() {

        return read;
    }


    public void setRead(boolean read) {

        this.read = read;
    }
//private Date date;



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


//    public Date getDate() {
//
//        return date;
//    }


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


    public void setReceiver(User receiver) {

        this.receiver = receiver;
    }


//    public void setDate(Date date) {
//
//        this.date = date;
//    }


    public void setAttach(File attach) {

        this.attach = attach;
    }


    public void setSubject(String subject) {

        this.subject = subject;
    }


    public void setMessage(String message) {

        this.message = message;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gmail gmail = (Gmail) o;
        return Objects.equals(localDateTime, gmail.localDateTime) &&
                Objects.equals(subject, gmail.subject) &&
                Objects.equals(message, gmail.message);
    }


    @Override
    public int hashCode() {

        return Objects.hash(localDateTime, subject, message);
    }


    @Override
    public String toString() {

        return "Gmail{" + subject + '\'' +
                ", message='" + message + '\'' ;
    }
}
