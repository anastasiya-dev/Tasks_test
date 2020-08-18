package by.academy.it.mysql;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDataSource {
    static Properties properties = new Properties();
    static String url = "jdbc:mysql://localhost:3306/client";

    static {
        properties.put("user", "root");
        properties.put("password", "Atme3816liveon!");
        properties.put("useSSL", "false");
        properties.put("serverTimezone", "UTC");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (
                ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, properties);
    }

    private static String testUrl = "jdbc:mysql://localhost:3306/client_test";

    public static Connection getTestConnection() throws SQLException {
        return DriverManager.getConnection(testUrl, properties);
    }
}

