package by.academy.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDataSource {
    static Properties properties = new Properties();
    static String url = "jdbc:mysql://localhost:3306/sql_homework";
    static String urlTest = "jdbc:mysql://localhost:3306/sql_homework_test";

    static {
        properties.put("user", "root");
        properties.put("password", "Atme3816liveon!");
        properties.put("useSSL", "false");
        properties.put("serverTimezone", "UTC");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, properties);
    }

    public static Connection getTestConnection() throws SQLException {
        return DriverManager.getConnection(urlTest, properties);
    }
}
