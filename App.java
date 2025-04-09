
import java.math.BigDecimal;
import java.util.Scanner;

class App {
    private static BigDecimal BANKBALANCE_DECIMAL = new BigDecimal(10000000);
    private static final CustomLogger logger = new CustomLogger("logs");

    public static void main(String[] args) {
        logger.log("INFO", "Bank Application Has Started");

        System.out.println("Welcome to The Bank Of Francis");
        App app = new App();
        try (Scanner scanner = new Scanner(System.in)) {
            app.mainMenu(scanner);
        }

        logger.shutdown();
    }

    private void mainMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n\t1. Deposit");
            System.out.println("\t2. Withdraw");
            System.out.println("\t3. View Balance");
            System.out.println("\t4. Exit");
            System.out.print("\tEnter Your Choice: ");
            int choice;
            if (!scanner.hasNextInt()) {
                System.out.println("\n\tInvalid input! Please enter a valid number.");
                logger.log("Error", "User Entered an Invalid value on the scanner that is expecting integer.");
                scanner.next(); // consume the leftover buffer
                continue;
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> deposit(scanner);
                case 2 -> withdraw(scanner);
                case 3 -> viewBalance();
                case 4 -> {
                    logger.log("INFO", "Bank Application is Exiting!");
                    System.out.println("\n\tExiting ...");
                    logger.log("INFO", "Bank Application has Exited successfully!");
                    System.exit(0);
                }
                default -> System.out.println("\n\tInvalid Choice!");
            }
        }

    }

    private void withdraw(Scanner scanner) {
        System.out.println("\n\t\tWithdraw");
        if (BANKBALANCE_DECIMAL.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("\n\tBank Balance is Zero: Sorry You can't Withdraw!");
            return;
        }
        System.out.println("\tBank Balance: $" + BANKBALANCE_DECIMAL);
        System.out.print("\tEnter amount to Withdraw: ");

        BigDecimal withdrawAmount;

        if (!scanner.hasNextBigDecimal()) {
            System.out.println("\n\tInvalid input! Please enter a valid number.");
            logger.log("Error", "User Entered an Invalid value on the scanner that is expecting BigDecimal.");
            scanner.nextLine();
        }

        withdrawAmount = scanner.nextBigDecimal();

        if (withdrawAmount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("\n\tSorry, withdraw amount can't be a negative number");
            logger.log("Error", "User tried to made a negative withdrawal");
            return;
        }

        if (BANKBALANCE_DECIMAL.compareTo(withdrawAmount) < 0) {
            System.out.println("\n\tSorry, You can't withdraw amount greater than Bank Balance");
            logger.log("Error", "User tried to Withdraw more than the bank balance");
            System.out.println("\tBank Balance: $" + BANKBALANCE_DECIMAL);
            System.out.println("\tWithdraw Amount: " + withdrawAmount);
            return;
        }
        BANKBALANCE_DECIMAL = BANKBALANCE_DECIMAL.subtract(withdrawAmount);
        System.out.println("\n\tWithdrew: " + withdrawAmount + " Successfully!");
        logger.log("SUCCESS", "User has withdrawn $" + withdrawAmount + " from the bank");
    }

    private void deposit(Scanner scanner) {
        scanner.nextLine();
        System.out.println("\n\tDeposit");
        System.out.print("\tEnter the Amount: ");

        BigDecimal depositAmount;

        if (!scanner.hasNextBigDecimal()) {
            System.out.println("\n\tInvalid input! Please enter a valid number.");
            logger.log("Error", "User Entered an Invalid value on the scanner that is expecting BigDecimal.");
            scanner.nextLine();
        }

        depositAmount = scanner.nextBigDecimal();

        if (depositAmount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("\n\tInvalid input! Please enter a positive number.");
            logger.log("Error", "User Deposited a negative value");
            return;
        }
        
        BANKBALANCE_DECIMAL = BANKBALANCE_DECIMAL.add(depositAmount);
        System.out.println("\n\tDeposited: " + depositAmount + " Successfully!");
        logger.log("SUCCESS", "User has deposited $" + depositAmount + " to the bank");
    }

    private void viewBalance() {
        System.out.println("\n\tBank Account Current Balance: " + BANKBALANCE_DECIMAL);
        logger.log("SUCCESS", "User has requested to view the bank balance, currently at: $" + BANKBALANCE_DECIMAL);

    }
}