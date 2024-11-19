import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/SimpleBankingSystem";
    private static final String USER = "root"; // Change as needed
    private static final String PASS = "ihatemysql"; // Change as needed

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {

            Bank bank = new Bank(" Simple Bank");
            System.out.println("Welcome to " + bank.getName());

            while (true) {
                System.out.println("1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. View Transaction History");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                try {
                    switch (choice) {
                        case 1:
                            System.out.print("Enter account number: ");
                            String accountNumber = scanner.next();
                            System.out.print("Enter name: ");
                            String name = scanner.next();
                            System.out.print("Enter initial balance: ");
                            double balance = scanner.nextDouble();
                            User user = new User(accountNumber, name, balance);
                            user.saveToDB(conn);
                            break;

                        case 2:
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            System.out.print("Enter amount: ");
                            double depositAmount = scanner.nextDouble();
                            Transaction deposit = new Transaction("DEPOSIT", depositAmount, accountNumber);
                            deposit.saveToDB(conn);
                            break;

                        case 3:
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            System.out.print("Enter amount: ");
                            double withdrawAmount = scanner.nextDouble();
                            Transaction withdrawal = new Transaction("WITHDRAWAL", withdrawAmount, accountNumber);
                            withdrawal.saveToDB(conn);
                            break;

                        case 4:
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            bank.viewTransactionHistory(conn, accountNumber);
                            break;

                        case 5:
                            System.out.println("Exiting...");
                            return;

                        default:
                            System.out.println("Invalid option. Try again.");
                    }
                } catch (Exception e) {
                    Logger.logError(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
