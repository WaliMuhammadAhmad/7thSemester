import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bank {
    private String name;

    // Constructor
    public Bank(String name) {
        this.name = name;
    }

    String getName(){return this.name;}

    public void viewTransactionHistory(Connection conn, String accountNumber) throws SQLException {
        String query = "SELECT t.transaction_type, t.amount, t.transaction_date " +
                "FROM Transactions t JOIN Accounts a ON t.account_id = a.account_id " +
                "WHERE a.account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Transaction History:");
            while (rs.next()) {
                System.out.println(rs.getString("transaction_type") + " of " +
                        rs.getDouble("amount") + " on " +
                        rs.getTimestamp("transaction_date"));
            }
        }
    }
}
