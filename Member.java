/**
 * # Member class that extends the User class
 */
public class Member extends User {

    /**
     * # Member class constructor
     * 
     * @param username
     * @param password
     */
    public Member(String username, String password) {
        super(username, password, Role.MEMBER);
    }
}