
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * # User manager Class
 */
public class UserManager {
    private final List<Member> members;
    private final List<AdminUser> adminUsers;
    private static final CustomFileHandler fileHandler = new CustomFileHandler();

    /**
     * # User Manager Class Constructor
     */
    public UserManager() {
        this.members = new ArrayList<>();
        this.adminUsers = new ArrayList<>();
    }

    /**
     * # Method to filter Admin User
     * 
     * @param username
     * @return
     */
    private AdminUser getAdminUser(String username) {
        for (AdminUser adminUser : adminUsers) {
            if (adminUser.getUsername().equals(username)) {
                return adminUser;
            }
        }
        return null;
    }

    /**
     * # Method to filter Member User
     * 
     * @param username
     * @return
     */
    private Member getMember(String username) {
        for (Member member : members) {
            if (member.getUsername().equals(username)) {
                return member;
            }
        }
        return null;
    }

    /**
     * Method to authenticate Members
     * 
     * @param username
     * @param password
     * @return
     */
    public Member authenticateMember(String username, String password) {
        Member member = getMember(username);

        if (member != null) {
            if (member.getHashedPassword().equals(hashPassword(password))) {
                return member;
            }
        }
        return null;
    }

    /**
     * Method to authenticate Admin Users
     * 
     * @param username
     * @param password
     * @return
     */
    public AdminUser authenticatAdminUser(String username, String password) {
        AdminUser adminUser = getAdminUser(username);

        if (adminUser != null) {
            if (adminUser.getHashedPassword().equals(hashPassword(password))) {
                return adminUser;
            }
        }
        return null;
    }

    /**
     * Method for adding New Members
     * 
     * @param username
     * @param password
     * @return
     */
    public Member addNewMember(String username, String password) {
        if (getMember(username) == null && getAdminUser(username) == null) {
            String hashedPassword = hashPassword(password);
            Member member = new Member(username, hashedPassword);
            members.add(member);
            fileHandler.saveMembersToFile(members);
            List<Member> newMembers = fileHandler.readMembersFormFile();
            members.clear();
            members.addAll(newMembers);
            return member;
        }
        return null;
    }

    /**
     * Method for adding New Admin Users
     * 
     * @param username
     * @param password
     * @return
     */
    public AdminUser addAdminUser(String username, String password) {
        if (getAdminUser(username) == null && getMember(username) == null) {
            String hashedPassword = hashPassword(password);
            AdminUser adminUser = new AdminUser(username, hashedPassword);
            adminUsers.add(adminUser);
            fileHandler.saveAdminsToFile(adminUsers);
            List<AdminUser> newAdminUsers = fileHandler.readAdminsFromFile();
            adminUsers.clear();
            adminUsers.addAll(newAdminUsers);
            return adminUser;
        }
        return null;
    }

    /**
     * # A method to hash password for security purposes
     * 
     * @param password
     * @return
     */
    public final String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * @param Member
     */
    public void loadUsersFromFile() {
        List<Member> newMembers = fileHandler.readMembersFormFile();
        List<AdminUser> newAdminUsers = fileHandler.readAdminsFromFile();

        members.clear();
        adminUsers.clear();

        members.addAll(newMembers);
        adminUsers.addAll(newAdminUsers);
    }
}
