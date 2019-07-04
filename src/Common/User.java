package Common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String lastName;
    private String username;
    private String password;
    private int age;
    private Gender gender;
    private int phoneNumber;

    public ArrayList<UserMail> mails = new ArrayList<>();


    public ArrayList<UserMail> getMails() {

        return mails;
    }


    public User(String username) {

        this.username = username;
    }


    public void setMails(ArrayList<UserMail> mails) {

        this.mails = mails;
    }


    public User(String name, String lastName, String username, String password, int age) {

        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.age = age;
    }


//    public byte[] getImage() {
//
//        return image;
//    }
//
//
//    public void setImage(byte[] image) {
//
//        this.image = image;
//    }


    public void setPhoneNumber(int phoneNumber) {

        this.phoneNumber = phoneNumber;
    }


    public int getPhoneNumber() {

        return phoneNumber;
    }


    public Gender getGender() {

        return gender;
    }


    public void setGender(Gender gender) {

        this.gender = gender;
    }


    public void setName(String name) {

        this.name = name;
    }


    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    public void setUsername(String username) {

        this.username = username;
    }


    public void setPassword(String password) {

        this.password = password;
    }


    public void setAge(int age) {

        this.age = age;
    }


    public String getName() {

        return name;
    }


    public String getLastName() {

        return lastName;
    }


    public String getUsername() {

        return username;
    }


    public String getPassword() {

        return password;
    }


    public int getAge() {

        return age;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }


    @Override
    public String toString() {

        return "User{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", phoneNumber=" + phoneNumber +
//                ", image=" + Arrays.toString(image) +
                ", mails=" + mails +
                '}';
    }
}

