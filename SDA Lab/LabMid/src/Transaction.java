import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {
    private String type; // "DEPOSIT" or "WITHDRAWAL"
    private double amount;
    private String accountNumber;

    // Constructor
    public Transaction(String type, double amount, String accountNumber) {
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void saveToDB(Connection conn) throws SQLException, InvalidAccountNumberException {
        String selectQuery = "SELECT account_id, balance FROM Accounts WHERE account_number = ?";
        String insertTransactionQuery = "INSERT INTO Transactions (account_id, transaction_type, amount) VALUES (?, ?, ?)";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            selectStmt.setString(1, this.accountNumber);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                double balance = rs.getDouble("balance");

                if ("WITHDRAWAL".equals(this.type) && balance < this.amount) {
                    throw new InsufficientFundsException("Insufficient funds for withdrawal.");
                }

                double newBalance = "DEPOSIT".equals(this.type) ? balance + this.amount : balance - this.amount;
                try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE Accounts SET balance = ? WHERE account_id = ?")) {
                    updateStmt.setDouble(1, newBalance);
                    updateStmt.setInt(2, accountId);
                    updateStmt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try (PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery)) {
                    insertStmt.setInt(1, accountId);
                    insertStmt.setString(2, this.type);
                    insertStmt.setDouble(3, this.amount);
                    insertStmt.executeUpdate();
                }
                Logger.logTransaction(this.type + " of " + this.amount + " for account " + this.accountNumber);
                System.out.println(this.type + " of " + this.amount + " for account " + this.accountNumber);
            } else {
                throw new InvalidAccountNumberException("Invalid account number.");
            }
        }
    }
}
