package by.academy;

import by.academy.mysql.DaoImpl;

import java.security.InvalidParameterException;
import java.sql.SQLException;

public class DaoFactory {
    private static DaoImpl dao;

    public static DaoImpl getDaoImpl(String database) throws SQLException {
        if ("mysql".equals(database)) {
            if (dao == null) {
                dao = new DaoImpl();
            }
            return dao;
        }
        if ("mysql_test".equals(database)) {
            if (dao == null) {
                dao = new DaoImpl();
            }
            return dao;

        }else {
            throw new InvalidParameterException("No such DB");
        }
    }
}
