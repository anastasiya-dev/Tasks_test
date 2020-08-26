package by.academy.mysql;

import by.academy.Dao;
import by.academy.DtoExpense;
import by.academy.DtoReceiver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class DaoImpl implements Dao {
    private Connection connection;
    private boolean isTestInstance;
    private static Logger log = Logger.getLogger(DaoImpl.class.getName());


    @Override
    public DtoReceiver getReceiver(int num) throws SQLException {
        return null;
    }

    @Override
    public List<DtoReceiver> getReceivers() throws SQLException {
        return null;
    }

    @Override
    public DtoExpense getExpense(int num) throws SQLException {
        return null;
    }

    @Override
    public List<DtoExpense> getExpenses() throws SQLException {
        return null;
    }

    @Override
    public int addReceiver(DtoReceiver receiver) throws SQLException {
        return 0;
    }

    @Override
    public int addExpense(DtoExpense expense) throws SQLException {
        return 0;
    }
}
