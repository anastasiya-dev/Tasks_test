package by.academy.mysql;

import by.academy.Dao;
import by.academy.DtoExpense;
import by.academy.DtoReceiver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DaoImpl implements Dao {

    private Connection connection;
    private boolean isTestInstance;

    public DaoImpl() throws SQLException {
        this.connection = MySqlDataSource.getConnection();
        Statement truncateStatement = connection.createStatement();
        String truncateString1 = "truncate expenses";
        truncateStatement.execute(truncateString1);
        String truncateString2 = "truncate receivers";
        truncateStatement.execute(truncateString2);
    }

    public DaoImpl(boolean isTestInstance) throws SQLException{
        this.isTestInstance = isTestInstance;
        this.connection = MySqlDataSource.getTestConnection();
    }


    @Override
    public DtoReceiver getReceiver(int num) throws SQLException {
        DtoReceiver dtoReceiver = new DtoReceiver();
        dtoReceiver.setNum(num);

        PreparedStatement preparedStatement = connection.prepareStatement("select * from receivers where num=?");
        preparedStatement.setInt(1, num);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<DtoReceiver> dtoReceiverList = parseReceiver(resultSet);
        preparedStatement.close();

        return dtoReceiverList.size() > 0 ? dtoReceiverList.get(0) : null;
    }

    @Override
    public List<DtoReceiver> getReceivers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from receivers");
        List<DtoReceiver> dtoReceiverList = parseReceiver(resultSet);
        statement.close();
        return dtoReceiverList;
    }

    @Override
    public DtoExpense getExpense(int num) throws SQLException {
        DtoExpense dtoExpense = new DtoExpense();
        dtoExpense.setNum(num);

        PreparedStatement preparedStatement = connection.prepareStatement("select * from expenses where num=?");
        preparedStatement.setInt(1, num);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<DtoExpense> dtoExpenseList = parseExpense(resultSet);
        preparedStatement.close();

        return dtoExpenseList.size() > 0 ? dtoExpenseList.get(0) : null;
    }

    @Override
    public List<DtoExpense> getExpenses() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from expenses");
        List<DtoExpense> dtoExpenseList = parseExpense(resultSet);
        statement.close();
        return dtoExpenseList;
    }

    @Override
    public int addReceiver(DtoReceiver dtoReceiver) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into receivers values (?,?)");
        preparedStatement.setInt(1, dtoReceiver.getNum());
        preparedStatement.setString(2, dtoReceiver.getName());
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        if (result) {
            return dtoReceiver.getNum();
        } else {
            return -1;
        }
    }

    @Override
    public int addExpense(DtoExpense dtoExpense) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into expenses values (?,?,?,?)");
        preparedStatement.setInt(1, dtoExpense.getNum());
        preparedStatement.setDate(2, dtoExpense.getPaydate());
        preparedStatement.setInt(3, dtoExpense.getReceiver());
        preparedStatement.setDouble(4, dtoExpense.getValue());
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        if (result) {
            return dtoExpense.getNum();
        } else {
            return -1;
        }
    }

    private List<DtoExpense> parseExpense(ResultSet resultSet) throws SQLException {
        List<DtoExpense> dtoExpenseList = new ArrayList<>();
        while (resultSet.next()) {
            DtoExpense dtoExpense = new DtoExpense();
            dtoExpense.setNum(resultSet.getInt("num"));
            dtoExpense.setPaydate(resultSet.getDate("paydate"));
            dtoExpense.setReceiver(resultSet.getInt("receiver"));
            dtoExpense.setValue(resultSet.getDouble("value"));
            dtoExpenseList.add(dtoExpense);
        }
        return dtoExpenseList;
    }

    private List<DtoReceiver> parseReceiver(ResultSet resultSet) throws SQLException {
        List<DtoReceiver> dtoReceiverList = new ArrayList<>();
        while (resultSet.next()) {
            DtoReceiver dtoReceiver = new DtoReceiver();
            dtoReceiver.setNum(resultSet.getInt("num"));
            dtoReceiver.setName(resultSet.getString("name"));
            dtoReceiverList.add(dtoReceiver);
        }
        return dtoReceiverList;
    }
}
