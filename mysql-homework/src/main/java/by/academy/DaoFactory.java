package by.academy;

import by.academy.mysql.DaoImpl;

import java.security.InvalidParameterException;
import java.sql.SQLException;

public class DaoFactory {
    private static DaoImpl dao;

    public static Dao getClientDao(String database) throws SQLException {
        if ("mysql-homework".equals(database)) {
            if (dao == null) {
                //lazy initialization
                dao = new DaoImpl();
            }
            return dao;
        } else if ("mysql-homework-test".equals(database)) {
            if (dao == null) {
                dao = new DaoImpl(true);
            }
            return dao;
        }
        throw new InvalidParameterException("No such database implemented " + database);
    }
}

