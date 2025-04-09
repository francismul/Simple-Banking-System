/**
 * # User Base Class
 */
public class User {

    /**
     * user class variables
     */
    private final String username;
    private final String password;
    private final Role role;

    /**
     * class Constructor
     *
     * @param username
     * @param password
     * @param role
     */
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Get Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the role assigned to a certain user
     *
     * @return
     */
    public Role getRole() {
        return role;
    }

    /**
     * Get hashed password
     *
     * @return
     */
    public String getHashedPassword() {
        return password;
    }

    /**
     * Method to help find if user is an admin
     *
     * @return boolean
     */
    public boolean isLibrarian() {
        return role == Role.ADMIN;
    }

}