package Common;

import java.io.Serializable;

//A class for holding all of a users records from the user database
public class User implements Serializable {


    //Variables for holding the user data
    private String username;
    private String password;
    private String email;
    private int lockCount;
    private int loggedIn;

    //Default constructor
    public User() {
    }

    //Overloaded constructors
    public User(String username, String password, String email, int lockCount, int loggedIn) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.lockCount = lockCount;
        this.loggedIn = loggedIn;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername){
        this.username = newUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLockCount() {
        return lockCount;
    }

    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }

    public int getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(int loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void print(){
        System.out.println("Username: " + this.username);
        System.out.println("Password: " + this.password);
        System.out.println("Email: " + this.email);
        System.out.println("Lock Count: " + this.lockCount);
        System.out.println("Loggedin: " + this.loggedIn);
    }

}