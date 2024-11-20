import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {
    private String type; // DEPOSIT or WITHDRAWAL
    private double amount;
    private User user;

    // Constructor
    public Transaction(String type, double amount, User user) {
        this.type = type;
        this.amount = amount;
        this.user = user;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public void saveToDatabase(Connection conn) throws SQLException {
        String query = "INSERT INTO Transactions (account_id, transaction_type, amount) " +
                "VALUES ((SELECT account_id FROM Accounts WHERE account_number = ?), ?, ?)";
        try (var stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getAccountNumber());
            stmt.setString(2, this.type);
            stmt.setDouble(3, this.amount);
            stmt.executeUpdate();
        }
        Logger.logTransaction(this.type + " of " + this.amount + " for account " + user.getAccountNumber());
    }
}
