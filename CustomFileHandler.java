import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomFileHandler {
    private static final CustomLogger logger = new CustomLogger("logs");

    /**
     * Method for writing Bank Account Details
     * 
     * @param lineString
     * @param member
     */
    public void writeToBankFile(String bankBalance, Member member) {
        String bankFile = member.getUsername() + "_account_details.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bankFile))) {
            writer.write(member.getUsername() + "," + bankBalance);
            logger.log("INFO", "Saved " + member.getUsername() + " details to file");
            writer.newLine();
        } catch (IOException e) {

        }
    }

    /**
     * Method for reading Bank Account Details
     * 
     * @param members
     */
    public String readFromBankFile(Member member) {
        String bankFile = member.getUsername() + "_account_details.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(bankFile))) {
            String line = reader.readLine();
            logger.log("INFO", "Read from file " + bankFile + ": " + line);
            String[] data = line.split(",");
            String bankBalanceValue = data[1];
            return bankBalanceValue;
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * Method for saving Members to file
     * 
     * @param members
     */
    public void saveMembersToFile(List<Member> members) {
        String filename = "members.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Member member : members) {
                writer.write(member.getUsername() + "," + member.getHashedPassword());
                logger.log("INFO", "Saved Member: " + member.getUsername());
                writer.newLine();
            }
        } catch (IOException e) {

        }
    }

    /**
     * Method for saving Admin Users to file
     * 
     * @param adminUsers
     */
    public void saveAdminsToFile(List<AdminUser> adminUsers) {
        String filename = "admins.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AdminUser adminUser : adminUsers) {
                writer.write(adminUser.getUsername() + "," + adminUser.getHashedPassword());
                logger.log("INFO", "Saved Admin: " + adminUser.getUsername());
                writer.newLine();
            }
        } catch (IOException e) {

        }
    }

    /**
     * Method for reading Members from File
     * 
     * @return
     */
    public List<Member> readMembersFormFile() {
        List<Member> members = new ArrayList<>();
        String filename = "members.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                logger.log("INFO", "Reading from file: " + filename);

                if (data.length == 2) {
                    Member member = new Member(data[0], data[1]);
                    members.add(member);
                }
            }
            return members;
        } catch (IOException e) {
            return members;
        }
    }

    /**
     * Method for reading Admin Users from File
     * 
     * @return
     */
    public List<AdminUser> readAdminsFromFile() {
        List<AdminUser> adminUsers = new ArrayList<>();
        String filename = "admins.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                logger.log("INFO", "Reading from file: " + filename);

                if (data.length == 2) {
                    AdminUser adminUser = new AdminUser(data[0], data[1]);
                    adminUsers.add(adminUser);
                }
            }
            return adminUsers;
        } catch (IOException e) {
            return adminUsers;
        }
    }
}
