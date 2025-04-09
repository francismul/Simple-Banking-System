
import java.io.File;
import java.math.BigDecimal;
import java.util.Scanner;

class App {
    private static BigDecimal BANKBALANCE_DECIMAL = new BigDecimal(0);
    private static final CustomLogger logger = new CustomLogger("logs");
    private static final CustomFileHandler fileHandler = new CustomFileHandler("bank.txt");

    public static void main(String[] args) {
        logger.log("INFO", "Bank Application has Started");

        System.out.println("Welcome to The Bank of Francis Mule");

        App app = new App();

        File bankFile = new File("bank.txt");

        if (bankFile.exists()) {
            String bankBalance = fileHandler.readFromFile();
            if (!bankBalance.isBlank() && !bankBalance.isBlank()) {
                BigDecimal newBankBalance = new BigDecimal(bankBalance);
                BANKBALANCE_DECIMAL = BANKBALANCE_DECIMAL.add(newBankBalance);
            }
        }

        try (Scanner scanner = new Scanner(System.in)) {
            app.mainMenu(scanner);
        }

        logger.shutdown();
    }

    private void mainMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Balance");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                logger.log("Error", "User Entered an Invalid value on the scanner that is expecting integer.");
                scanner.nextLine(); // consume the leftover buffer
                continue;
            }

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> deposit(scanner);
                case 2 -> withdraw(scanner);
                case 3 -> viewBalance();
                case 4 -> {
                    System.out.println("Exiting ...");
                    logger.log("INFO", "Bank Application has Exited successfully!");
                    System.exit(0);
                }
                default -> System.out.println("\n\tInvalid Choice!");
            }

        }
    }

    private void withdraw(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Welcome to Withdraw Department");

        if (BANKBALANCE_DECIMAL.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Bank Balance is Zero: Sorry You can't Withdraw!");
            logger.log("Error", "User tried to withdraw when the bank had zero balance");
            return;
        }

        System.out.println("Bank Balance: $" + BANKBALANCE_DECIMAL);
        System.out.print("Enter amount to Withdraw: ");

        BigDecimal withdrawAmount;

        if (!scanner.hasNextBigDecimal()) {
            System.out.println("Invalid input! Please enter a valid number.");
            logger.log("Error", "User Entered an Invalid value on the scanner that is expecting BigDecimal.");
            scanner.nextLine();
        }

        withdrawAmount = scanner.nextBigDecimal();

        if (withdrawAmount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("\nSorry, withdraw amount can't be a negative number");
            logger.log("Error", "User tried to made a negative withdrawal");
            return;
        }

        if (BANKBALANCE_DECIMAL.compareTo(withdrawAmount) < 0) {
            System.out.println("Sorry, You can't withdraw amount greater than Bank Balance");
            logger.log("Error", "User tried to Withdraw more than the bank balance");
            System.out.println("Bank Balance: $" + BANKBALANCE_DECIMAL);
            System.out.println("Withdraw Amount: " + withdrawAmount);
            return;
        }

        BANKBALANCE_DECIMAL = BANKBALANCE_DECIMAL.subtract(withdrawAmount);

        fileHandler.writeToFile("" + BANKBALANCE_DECIMAL);

        System.out.println("Withdrew: " + withdrawAmount + " Successfully!");
        logger.log("SUCCESS", "User has withdrawn $" + withdrawAmount + " from the bank");
    }

    private void deposit(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Welcome to Deposit Department");
        System.out.print("Enter Deposit Amount: ");

        BigDecimal depositAmount;

        if (!scanner.hasNextBigDecimal()) {
            System.out.println("Invalid input! Please enter a valid number.");
            logger.log("Error", "User Entered an Invalid value on the scanner that is expecting BigDecimal.");
            scanner.nextLine();
        }

        depositAmount = scanner.nextBigDecimal();

        if (depositAmount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Invalid input! Please enter a positive number.");
            logger.log("Error", "User Deposited a negative value");
            return;
        }

        BANKBALANCE_DECIMAL = BANKBALANCE_DECIMAL.add(depositAmount);

        fileHandler.writeToFile("" + BANKBALANCE_DECIMAL);

        System.out.println("Deposited: " + depositAmount + " Successfully!");
        logger.log("SUCCESS", "User has deposited $" + depositAmount + " to the bank");
    }

    private void viewBalance() {
        System.out.println("Bank Account Current Balance: " + BANKBALANCE_DECIMAL);
        logger.log("SUCCESS", "User viewed Account Balance: $" + BANKBALANCE_DECIMAL);
    }
}
