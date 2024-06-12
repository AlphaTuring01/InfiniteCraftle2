package lab.prog.infinitecraftle.domain;

/**
 * Class that represents a user.
 * Contains the user's username, password and id
 */
public class User {
    private String username, password;
    private int id;

    /**
     * Constructor for User class
     * @param username username of the user
     * @param password password of the user
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.id = 0;
    }
    public User(int id){
        this.username = "";
        this.password = "";
        this.id = id;
    }

    /**
     * Setter for id of the user
     * @param id id of the user
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Getter for id of the user
     * @return id of the user
     */
    public int getId(){
        return id;
    }

    /**
     * Getter for username of the user
     * @return username of the user
     */
    public String getUsername(){
        return username;
    }

    /**
     * Getter for password of the user
     * @return password of the user
     */
    public String getPassword(){
        return password;
    }
}
