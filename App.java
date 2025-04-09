
import java.math.BigDecimal;
import java.util.Scanner;

public class App {
    private static final UserManager userManager = new UserManager();
    private static final CustomLogger logger = new CustomLogger("logs");

    public static void main(String[] args) {
        logger.log("INFO", "Bank Application has Started");

        App app = new App();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to The Bank of Francis Mule");

            app.mainMenu(scanner);
        }
    }

    /**
     * Main Menu Implementation
     * 
     * @param scanner
     */
    private void mainMenu(Scanner scanner) {
        while (true) {
            userManager.loadUsersFromFile();

            System.out.println("Please Select an Option:");
            System.out.println("1. Member");
            System.out.println("2. Administrator");
            System.out.println("3. Register");
            System.out.println("4. Exit");
            System.out.print("Enter your option: ");

            int choice;
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting integer.");
                scanner.nextLine(); // consume the leftover buffer
                continue;
            }

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("Welcome Memeber");
                    System.out.println("Choose an option:");
                    System.out.println("1. Login");
                    System.out.println("2. Exit");
                    System.out.print("Enter your choice: ");
                    int role;

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a valid number.");
                        logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting integer.");
                        scanner.nextLine();
                        continue;
                    }

                    role = scanner.nextInt();

                    switch (role) {
                        case 1 -> loginMember(scanner);
                        case 2 -> {
                        }
                        default -> System.out.println("Invalid option");
                    }
                    // break;
                }
                case 2 -> {
                    System.out.println("Welcome Administrator");
                    System.out.println("Choose an option:");
                    System.out.println("1. Login");
                    System.out.println("2. Exit");
                    System.out.print("Enter your choice: ");
                    int role;

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a valid number.");
                        logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting integer.");
                        scanner.nextLine();
                        continue;
                    }

                    role = scanner.nextInt();

                    switch (role) {
                        case 1 -> loginAdminUsers(scanner);
                        case 2 -> {
                            System.out.println("Good Bye");
                        }
                        default -> System.out.println("Invalid option");
                    }
                }
                case 3 -> register(scanner);
                case 4 -> {
                    System.out.println("Application Exiting...");
                    logger.log("INFO", "Application Exited");
                    System.exit(0);
                }
                default -> System.out.println("YOU NEED HELP MY FRIEND!");
            }
        }
    }

    /**
     * Register Method
     * 
     * @param scanner
     */
    private void register(Scanner scanner) {
        scanner.nextLine();

        System.out.println("Welcome to Registration Department");

        System.out.print("Enter your username: ");
        String username;
        if (!scanner.hasNextLine()) {
            System.out.println("Invalid input! Please enter a username.");
            logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting a String.");
            scanner.nextLine();
        }
        username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password;
        if (!scanner.hasNext()) {
            System.out.println("Invalid input! Please enter a password.");
            logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting a String.");
            scanner.nextLine();
        }
        password = scanner.next();

        System.out.print("Enter your role (1 for Administrator, 2 for Member): ");
        int role;
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a valid number.");
            logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting integer.");
            scanner.nextLine();
        }
        role = scanner.nextInt();

        switch (role) {
            case 1 -> {
                AdminUser adminUser = userManager.addAdminUser(username, password);
                if (adminUser != null) {
                    System.out.println("Admin User added successfully");
                    System.out.println("Login to continue");
                    loginAdminUsers(scanner);
                } else {
                    System.out.println("User with similar username already exists.");
                    register(scanner);
                }
            }
            case 2 -> {
                Member member = userManager.addNewMember(username, password);
                if (member != null) {
                    System.out.println("Member added successfully");
                    System.out.println("Login to continue");
                    loginMember(scanner);
                } else {
                    System.out.println("User with similar username already exists.");
                    register(scanner);
                }
            }
            default -> System.out.println("Are you serious right now!");
        }
    }

    /**
     * Login Admin Users Method
     * 
     * @param scanner
     */
    private void loginAdminUsers(Scanner scanner) {
        scanner.nextLine();

        System.out.print("Enter your username: ");
        String username;
        if (!scanner.hasNextLine()) {
            System.out.println("Invalid input! Please enter a username.");
            logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting a String.");
            scanner.nextLine();
        }
        username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password;
        if (!scanner.hasNext()) {
            System.out.println("Invalid input! Please enter a password.");
            logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting a String.");
            scanner.nextLine();
        }
        password = scanner.next();

        AdminUser adminUser = userManager.authenticatAdminUser(username, password);

        if (adminUser != null) {
            System.out.println("You have logged in successfully");
            logger.log("INFO", "User: " + adminUser.getUsername() + " Loged in successfully!");

            adminLogic(scanner, adminUser);
        } else {
            System.out.println("Invalid Credentials");
            logger.log("ERROR",
                    "Log in Attempt with incorrect incredentials; username: " + username + "; Password: " + password);
        }
    }

    /**
     * Login Members Method
     * 
     * @param scanner
     */
    private void loginMember(Scanner scanner) {
        scanner.nextLine();

        System.out.print("Enter your username: ");
        String username;
        if (!scanner.hasNextLine()) {
            System.out.println("Invalid input! Please enter a username.");
            logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting a String.");
            scanner.nextLine();
        }
        username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password;
        if (!scanner.hasNext()) {
            System.out.println("Invalid input! Please enter a password.");
            logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting a String.");
            scanner.nextLine();
        }
        password = scanner.next();

        Member member = userManager.authenticateMember(username, password);

        if (member != null) {
            System.out.println("You have logged in successfully");
            logger.log("INFO", "User: " + member.getUsername() + " Loged in successfully!");
            memberLogic(scanner, member);
        } else {
            System.out.println("Invalid Credentials");
            logger.log("ERROR",
                    "Log in Attempt with incorrect incredentials; username: " + username + "; Password: " + password);
        }
    }

    /**
     * Members Logic
     * 
     * @param scanner
     * @param member
     */
    private void memberLogic(Scanner scanner, Member member) {
        System.out.println("Bonjour! " + member.getUsername());
        Bank bank = new Bank(member);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Deposit Amount");
            System.out.println("2. Withdraw Amout");
            System.out.println("3. Display Balance");
            System.out.println("4. Exit");
            System.out.print("Enter your option: ");

            int choice;
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting integer.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    scanner.nextLine();

                    System.out.println("Welcome to Deposit Department");
                    System.out.print("Enter Deposit Amount: ");

                    BigDecimal depositAmount;
                    if (!scanner.hasNextBigDecimal()) {
                        System.out.println("Invalid input! Please enter a valid number.");
                        logger.log("ERROR",
                                "User Entered an Invalid value on the scanner that is expecting BigDecimal.");
                        scanner.nextLine();
                        continue;
                    }

                    depositAmount = scanner.nextBigDecimal();

                    if (depositAmount.compareTo(BigDecimal.ZERO) < 0) {
                        System.out.println("Invalid input! Please enter a positive number.");
                        logger.log("Error", "User Deposited a negative value");
                        break;
                    }

                    int status = bank.deposit(depositAmount);
                    if (status > 0) {
                        System.out.println("Deposited: " + depositAmount + " Successfully!");
                        logger.log("SUCCESS",
                                "User: " + member.getUsername() + " has deposited $" + depositAmount + " to the bank");
                    }
                }
                case 2 -> {
                    scanner.nextLine();
                    System.out.println("Welcome to Withdraw Department");

                    if (bank.BANK_BALANCE.compareTo(BigDecimal.ZERO) == 0) {
                        System.out.println("Bank Balance is Zero: Sorry You can't Withdraw!");
                        logger.log("ERROR", "User tried to withdraw when the bank had zero balance");
                        break;
                    }

                    System.out.println("Available Bank Balance: $" + bank.BANK_BALANCE);
                    System.out.print("Enter amount to Withdraw: ");

                    BigDecimal withdrawAmount;

                    if (!scanner.hasNextBigDecimal()) {
                        System.out.println("Invalid input! Please enter a valid number.");
                        logger.log("ERROR",
                                "User Entered an Invalid value on the scanner that is expecting BigDecimal.");
                        scanner.nextLine();
                        continue;
                    }

                    withdrawAmount = scanner.nextBigDecimal();

                    if (withdrawAmount.compareTo(BigDecimal.ZERO) < 0) {
                        System.out.println("\nSorry, withdraw amount can't be a negative number");
                        logger.log("Error", "User tried to made a negative withdrawal");
                        break;
                    }
                    
                    int status = bank.withdraw(withdrawAmount);

                    if (status > 0) {
                        System.out.println("Withdrew: " + withdrawAmount + " Successfully!");
                        logger.log("SUCCESS", "User: " + member.getUsername() + " has withdrawn $" + withdrawAmount
                                + " from the bank");
                    }
                }
                case 3 -> bank.viewBalance();
                case 4 -> {
                    System.out.println("Hasta la vista, baby! (Terminator Style!)");
                    return;
                }
                default -> System.out.println("Are you dumb!");
            }
        }
    }

    /**
     * Admins Logic
     * 
     * @param scanner
     * @param adminUser
     */
    private void adminLogic(Scanner scanner, AdminUser adminUser) {
        System.out.println("Hola! " + adminUser.getUsername());

        while (true) {
            System.out.println("1. Register New User");
            System.out.println("2. Remove User");
            System.out.println("3. View Users");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int role;

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                logger.log("ERROR", "User Entered an Invalid value on the scanner that is expecting integer.");
                scanner.nextLine();
                continue;
            }

            role = scanner.nextInt();
            switch (role) {
                case 1 -> {
                    System.out.println("Hola! Our Esteemed Administrator");
                    System.out.println("Our team is working tirelessly to make this feature available!");
                    System.out.println("Expect something Great Soon!");
                    System.out.println("Catch you on the flip side!");
                }
                case 2 -> {
                    System.out.println("Hola! Our Prestigious Administrator");
                    System.out.println("Our team is working tirelessly to make this feature available!");
                    System.out.println("Expect something Great Soon!");
                    System.out.println("Stay frosty!");
                }
                case 3 -> {
                    System.out.println("Hola! Our Honored Administrator");
                    System.out.println("Our team is working tirelessly to make this feature available!");
                    System.out.println("Expect something Great Soon!");
                    System.out.println("Peace out, soldier!");
                }
                case 4 -> {
                    System.out.println("Adios, Amigo!");
                    return;
                }
                default -> System.out.println("What the hell men!");
            }
        }
    }
}
