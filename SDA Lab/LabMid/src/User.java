import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    private String accountNumber;
    private String name;
    private double balance;

    public User(String accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void saveToDB(Connection conn) throws SQLException, SQLException {
        String query = "INSERT INTO Accounts (account_number, name, balance) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.accountNumber);
            stmt.setString(2, this.name);
            stmt.setDouble(3, this.balance);
            stmt.executeUpdate();
            Logger.logTransaction("Account created for " + this.accountNumber);
            System.out.println("Account created for " + this.accountNumber);
        }
    }
}
