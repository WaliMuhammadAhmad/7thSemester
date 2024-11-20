import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private List<User> users;

    // Constructor
    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<>();
    }

    // Add a user to the bank
    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public String getName() {return this.name;}

    // Get all transactions for all users
    public List<Transaction> getAllTransactions() {
        List<Transaction> allTransactions = new ArrayList<>();
        for (User user : users) {
            allTransactions.addAll(user.getTransactions());
        }
        return allTransactions;
    }

    public List<User> getUser(Connection conn, String accountNumber) throws SQLException {
        String query = "SELECT * FROM Accounts WHERE account_number = ?";
        ResultSet rs;
        ArrayList<User> users = new ArrayList<>();

        try (var stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            rs = stmt.executeQuery();
        }

        while (rs.next()) {
            users.add(new User(rs.getString("account_number"), rs.getString("name"), rs.getDouble("balance")));
        }
        return users;
    }
}
