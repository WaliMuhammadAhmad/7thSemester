import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/SimpleBankingSystem";
    private static final String USER = "root";
    private static final String PASS = "ihatemysql";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {

            Bank bank = new Bank("Simple Bank");
            System.out.println("Welcome to " + bank.getName());

            while (true) {
                System.out.println("\n1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. View User Transaction History");
                System.out.println("5. View All Transactions (Bank Level)");
                System.out.println("6. Exit");
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
                            user.saveToDatabase(conn);
                            bank.addUser(user);
                            System.out.println("Account created successfully for " + name + ".");
                            break;

                        case 2:
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            System.out.print("Enter deposit amount: ");
                            double depositAmount = scanner.nextDouble();
                            User depositUser = findUserByAccountNumber(bank, accountNumber);
                            if (depositUser != null) {
                                Transaction deposit = new Transaction("DEPOSIT", depositAmount, depositUser);
                                deposit.saveToDatabase(conn);
                                depositUser.addTransaction(deposit);
                                System.out.println("Deposit of " + depositAmount + " successful.");
                            } else {
                                System.out.println("Account not found.");
                            }
                            break;

                        case 3:
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();
                            System.out.print("Enter withdrawal amount: ");
                            double withdrawAmount = scanner.nextDouble();

                            User withdrawUser = findUserByAccountNumber(bank, accountNumber);
                            if (withdrawUser != null) {
                                Transaction withdrawal = new Transaction("WITHDRAWAL", withdrawAmount, withdrawUser);
                                withdrawal.saveToDatabase(conn);
                                withdrawUser.addTransaction(withdrawal);

                                System.out.println("Withdrawal of " + withdrawAmount + " successful.");
                            } else {
                                System.out.println("Account not found.");
                            }
                            break;

                        case 4:
                            System.out.print("Enter account number: ");
                            accountNumber = scanner.next();

                            User historyUser = findUserByAccountNumber(bank, accountNumber);
                            if (historyUser != null) {
                                System.out.println("Transaction History for " + historyUser.getName() + ":");
                                for (Transaction transaction : historyUser.getTransactions()) {
                                    System.out.println(transaction.getType() + " of " +
                                            transaction.getAmount() + " by " + historyUser.getName());
                                }
                            } else {
                                System.out.println("Account not found.");
                            }
                            break;

                        case 5:
                            System.out.println("All Transactions (Bank Level):");
                            for (Transaction transaction : bank.getAllTransactions()) {
                                System.out.println(transaction.getType() + " of " +
                                        transaction.getAmount() + " by account " +
                                        transaction.getUser().getAccountNumber());
                            }
                            break;

                        case 6:
                            System.out.println("Exiting...");
                            return;

                        default:
                            System.out.println("Invalid option. Try again.");
                    }
                } catch (Exception e) {
                    Logger.logError(e.getMessage());
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    // utils
    private static User findUserByAccountNumber(Bank bank, String accountNumber) {
        for (User user : bank.getUsers()) {
            if (user.getAccountNumber().equals(accountNumber)) {
                return user;
            }
        }
        return null;
    }
}
