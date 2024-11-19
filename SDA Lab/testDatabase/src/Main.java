import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            // Create a new database
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS testDB";
            statement.executeUpdate(createDatabaseSQL);
            System.out.println("Database 'testDB' created successfully!");

            // Connect to the newly created database
            connection = DriverManager.getConnection(URL + "testDB", USER, PASSWORD);

            // Create a table in 'testDB'
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "email VARCHAR(100))";
            statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            System.out.println("Table 'users' created successfully!");

            // Insert data into the 'users' table
            String insertDataSQL = "INSERT INTO users (name, email) VALUES " +
                    "('John Doe', 'john.doe@example.com'), " +
                    "('Jane Smith', 'jane.smith@example.com')";
            statement.executeUpdate(insertDataSQL);
            System.out.println("Data inserted successfully!");

            // Close the connection
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
