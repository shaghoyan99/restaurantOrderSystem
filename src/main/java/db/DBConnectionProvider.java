package db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionProvider {

    @Getter
    private static final DBConnectionProvider instance = new DBConnectionProvider();
    private Connection connection;


    private DBConnectionProvider() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String JDBC_URL = "jdbc:mysql://localhost:3306/restaurantOrderSystem";
                String JDBC_USERNAME = "root";
                String JDBC_PASSWORD = "root";
                connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return connection;
    }

}
