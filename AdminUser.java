/**
 * # Admin User class that extends the User class
 */
public class AdminUser extends User {

    /**
     * # Admin User Class Constructor
     * 
     * @param username
     * @param password
     */
    public AdminUser(String username, String password) {
        super(username, password, Role.ADMIN);
    }
}
