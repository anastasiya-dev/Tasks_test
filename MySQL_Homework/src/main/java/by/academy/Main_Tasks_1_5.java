package by.academy;

import java.sql.*;
import java.util.Properties;

public class Main_Tasks_1_5 {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();
            properties.put("user", "root");
            properties.put("password", "Atme3816liveon!");
            //secure sockets layer
            //i.e. we are going to use unencrypted info
            properties.put("useSSL", "false");
            properties.put("serverTimezone", "UTC");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sql_homework",
                    properties);
            Statement statement = connection.createStatement();
            String queryAdd = "INSERT INTO expenses VALUES (101, \"2011-07-07\", 3, 20100)";
            String queryOut = "SELECT paydate, value, name FROM expenses, receivers" +
                    " WHERE receiver=receivers.num";
            statement.executeUpdate(queryAdd);
            ResultSet resultSet = statement.executeQuery(queryOut);
            while (resultSet.next()) {
                System.out.println(resultSet.getDate(1) + " "
                        + resultSet.getDouble(2) + " " +
                        resultSet.getString(3));
            }

            System.out.println("----------------------");
            String template = "SELECT paydate, value, name FROM expenses, receivers WHERE receiver=receivers.num AND value > ?";
            PreparedStatement preparedStatement = connection.prepareStatement(template);
            preparedStatement.setFloat(1, 10000.0F);

            ResultSet resultSetNew = preparedStatement.executeQuery();
            while (resultSetNew.next()) {
                System.out.println(resultSetNew.getDate(1) + " "
                        + resultSetNew.getDouble(2) + " " +
                        resultSetNew.getString(3));
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
