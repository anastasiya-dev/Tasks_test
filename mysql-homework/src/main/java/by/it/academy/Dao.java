package by.it.academy;


import java.sql.SQLException;
import java.util.List;

public interface Dao {
    DtoReceiver getReceiver(Integer num) throws SQLException;

    List<DtoReceiver> getReceivers() throws SQLException;

    DtoExpense getExpense(Integer num) throws SQLException;

    List<DtoExpense> getExpenses() throws SQLException;

    int addReceiver(DtoReceiver receiver) throws SQLException;

    int addExpense(DtoExpense expense) throws SQLException;
}
