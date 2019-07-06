package Common;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class UserMail implements Serializable {
    private static final long serialVersionUID = 10001L;
    private Gmail gmail;
    public Set<MailType> mailTypeSet;
    private UserMail previous;
    private UserMail next;


    @Override
    public String toString() {

        return "UserMail{" +
                "gmail=" + gmail +
                ", mailTypeSet=" + mailTypeSet +

                '}';
    }


    public UserMail getPrevious() {

        return previous;
    }


    public void setPrevious(UserMail previous) {

        this.previous = previous;
    }


    public UserMail getNext() {

        return next;
    }


    public void setNext(UserMail next) {

        this.next = next;
    }


    public Gmail getGmail() {

        return gmail;
    }


    public UserMail(Gmail gmail, Set<MailType> mailTypeSet) {

        this.gmail = gmail;
        this.mailTypeSet = mailTypeSet;
    }


    public UserMail(Gmail gmail) {

        this.gmail = gmail;
    }


    public void setGmail(Gmail gmail) {

        this.gmail = gmail;
    }


    public Set<MailType> getMailTypeSet() {

        return mailTypeSet;
    }


    public void setMailTypeSet(Set<MailType> mailTypeSet) {

        this.mailTypeSet = mailTypeSet;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMail userMail = (UserMail) o;
        return Objects.equals(gmail, userMail.gmail);
    }


    @Override
    public int hashCode() {

        return Objects.hash(gmail);
    }
}
