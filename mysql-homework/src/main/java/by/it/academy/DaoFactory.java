package by.it.academy;

import by.it.academy.mysql.DaoImpl;

import java.security.InvalidParameterException;
import java.sql.SQLException;

public class DaoFactory {
    private static DaoImpl dao;

    public static Dao getDaoImpl(String database) throws SQLException {
        if ("mysql_homework".equals(database)) {
            if (dao == null) {
                //lazy initialization
                dao = new DaoImpl();
            }
            return dao;
        }
        else if("mysql_homework_test".equals(database)){
            if (dao == null) {
                dao = new DaoImpl(true);
            }
            return dao;
        }
        throw new InvalidParameterException("No such database implemented " + database);
    }
}
