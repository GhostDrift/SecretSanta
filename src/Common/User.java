package Common;

import java.io.Serializable;
import java.util.ArrayList;

//A class for holding all of a users records from the user database
public class User implements Serializable {


    //Variables for holding the user data
    private String username;
    private String password;
    private String email;
    private int lockCount;
    private int loggedIn;
    private int id;
    private ArrayList<String> wishList;
    private int ssrid;
    private int removeID;
    private String entry;

    //Default constructor
    public User() {
    }

    //Overloaded constructors
    public User(int id,String username, String password, String email, int lockCount, int loggedIn, int ssrid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.lockCount = lockCount;
        this.loggedIn = loggedIn;
        this.id = id;
        this.wishList = new ArrayList<>();
        this.ssrid = ssrid;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User(String username){
        this.username = username;
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

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getSsrid(){
        return this.ssrid;
    }
    public void setSsrid(int id){
        this.ssrid = id;
    }
    public ArrayList<String> getWishList(){
        return this.wishList;
    }

    public void setWishList(ArrayList<String> wishList) {
        this.wishList = wishList;
    }
    public String getEntry(){
        return this.entry;
    }
    public void setEntry(String entry){
        this.entry = entry;
    }
    public int getRemoveID(){
        return this.removeID;
    }
    public void setRemoveID(int rid){
        this.removeID = rid;
    }

    public void print(){
        System.out.println("Username: " + this.username);
        System.out.println("Password: " + this.password);
        System.out.println("Email: " + this.email);
        System.out.println("Lock Count: " + this.lockCount);
        System.out.println("Loggedin: " + this.loggedIn);
    }

}