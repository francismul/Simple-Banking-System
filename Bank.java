import java.io.File;
import java.math.BigDecimal;

/**
 * Bank Class
 */
public class Bank {
    private final Member member;
    public BigDecimal BANK_BALANCE = new BigDecimal(0);
    private static final CustomLogger logger = new CustomLogger("logs");
    private static final CustomFileHandler fileHandler = new CustomFileHandler();

    /**
     * Bank Constructor
     * 
     * @param member
     */
    public Bank(Member member) {
        this.member = member;

        File bankFile = new File(member.getUsername() + "_account_details.txt");

        if (bankFile.exists()) {
            String bankBalance = fileHandler.readFromBankFile(member);
            if (!bankBalance.isBlank() && !bankBalance.isEmpty()) {
                BigDecimal newBankBalance = new BigDecimal(bankBalance);
                BANK_BALANCE = BANK_BALANCE.add(newBankBalance);
            }
        }
    }

    /**
     * Deposit Method
     * 
     * @param bankDeposit
     * @return
     */
    public int deposit(BigDecimal bankDeposit) {
        if (bankDeposit.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Invalid input! Please enter a positive number.");
            logger.log("Error", "User Deposited a negative value");
            return -1;
        }
        BANK_BALANCE = BANK_BALANCE.add(bankDeposit);
        fileHandler.writeToBankFile("" + BANK_BALANCE, member);
        return 0;
    }

    /**
     * Withdraw Method
     * 
     * @param withdrawAmount
     * @return
     */
    public int withdraw(BigDecimal withdrawAmount) {
        if (BANK_BALANCE.compareTo(withdrawAmount) < 0) {
            System.out.println("Sorry, You can't withdraw amount greater than Bank Balance");
            logger.log("Error", "User tried to Withdraw more than the bank balance");
            System.out.println("Bank Balance: $" + BANK_BALANCE);
            System.out.println("Withdraw Amount: $" + withdrawAmount);
            return -1;
        }
        BANK_BALANCE = BANK_BALANCE.subtract(withdrawAmount);
        fileHandler.writeToBankFile("" + BANK_BALANCE, member);
        return 0;
    }

    /**
     * Method for viewing Bank Balance
     */
    public void viewBalance() {
        System.out.println("Bank Account Current Balance: $" + BANK_BALANCE);
        logger.log("SUCCESS", "User: " + member.getUsername() + " viewed Account Balance: $" + BANK_BALANCE);
    }
}
