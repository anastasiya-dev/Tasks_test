package by.it.academy.mysql;

import by.it.academy.DaoFactory;
import by.it.academy.DtoExpense;
import by.it.academy.DtoReceiver;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DaoImplTest {

    private static DaoImpl dao = null;
    IDatabaseConnection connection;

    @Test
    public void testGetReceiver_equality() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DaoImplTest.class.getResourceAsStream("DaoImplTestReceivers.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        DtoReceiver receiver = (DtoReceiver) dao.getReceiver(2);

        //Then
        assertNotNull(receiver);
        assertEquals("Korona", receiver.getName());
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test(expected = Exception.class)
    public void testGetReceiver_nullNum() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DaoImplTest.class.getResourceAsStream("DaoImplTestReceivers.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        DtoReceiver receiver = (DtoReceiver) dao.getReceiver(null);

        //Then
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void testGetReceivers_all() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DaoImplTest.class.getResourceAsStream("DaoImplTestReceivers.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        List<DtoReceiver> receiversActual = new ArrayList<>();

        //info for receivers
        List<Integer> numsR = List.of(1, 2, 3);
        List<String> namesR = List.of("Solo", "Korona", "MTS");

        //adding receivers
        for (int i = 0; i < numsR.size(); i++) {
            DtoReceiver dtoReceiver = new DtoReceiver();
            dtoReceiver.setNum(numsR.get(i));
            dtoReceiver.setName(namesR.get(i));
            receiversActual.add(dtoReceiver);
        }

        //When
        List<DtoReceiver> receiversTest = dao.getReceivers();

        //Then
        assertNotNull(receiversTest);
        assertEquals(receiversActual.size(), receiversTest.size());
        for (int i = 0; i < receiversActual.size(); i++) {
            assertEquals(receiversActual.get(i).getNum(), receiversTest.get(i).getNum());
            assertEquals(receiversActual.get(i).getName(), receiversTest.get(i).getName());
        }
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void testGetExpense_equality() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DaoImplTest.class.getResourceAsStream("DaoImplTestExpenses.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        DtoExpense expense = (DtoExpense) dao.getExpense(2);

        //Then
        assertNotNull(expense);
        assertEquals(Date.valueOf("2011-05-10"), expense.getPaydate());
        assertEquals(2, expense.getReceiver());
        assertEquals(10000.0, expense.getValue(), 0);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test(expected = Exception.class)
    public void testGetExpense_nullNum() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DaoImplTest.class.getResourceAsStream("DaoImplTestExpenses.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        DtoExpense expense = (DtoExpense) dao.getExpense(null);

        //Then
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void testGetExpenses_all() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DaoImplTest.class.getResourceAsStream("DaoImplTestExpenses.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        List<DtoExpense> expensesActual = new ArrayList<>();

        //info for expenses
        List<Integer> numsE = List.of(1, 2, 3, 4, 5, 6);
        List<Date> datesE = List.of(java.sql.Date.valueOf("2011-05-10"),
                Date.valueOf("2011-05-10"), Date.valueOf("2011-05-11"), Date.valueOf("2011-05-11"),
                Date.valueOf("2011-07-07"), Date.valueOf("2020-07-15"));
        List<Integer> receiversE = List.of(1, 2, 3, 2, 3, 1);
        List<Double> valuesE = List.of(94200.5, 10000.0, 12950.0, 20100.0, 20100.0, 0.4);

        //adding expenses
        for (int i = 0; i < numsE.size(); i++) {
            DtoExpense dtoExpense = new DtoExpense();
            dtoExpense.setNum(numsE.get(i));
            dtoExpense.setPaydate(datesE.get(i));
            dtoExpense.setReceiver(receiversE.get(i));
            dtoExpense.setValue(valuesE.get(i));
            expensesActual.add(dtoExpense);
        }

        //When
        List<DtoExpense> expensesTest = dao.getExpenses();

        //Then
        assertNotNull(expensesTest);
        assertEquals(expensesActual.size(), expensesTest.size());
        for (int i = 0; i < expensesActual.size(); i++) {
            assertEquals(expensesActual.get(i).getNum(), expensesActual.get(i).getNum());
            assertEquals(expensesActual.get(i).getPaydate(), expensesActual.get(i).getPaydate());
            assertEquals(expensesActual.get(i).getReceiver(), expensesActual.get(i).getReceiver());
            assertEquals(expensesActual.get(i).getValue(), expensesActual.get(i).getValue(), 0);
        }
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void testAddReceiver() throws SQLException {
        //Given
        DtoReceiver receiverActual = new DtoReceiver();
        int numTest = 4;
        receiverActual.setNum(numTest);
        receiverActual.setName("GUM");
        dao.addReceiver(receiverActual);

        //When
        DtoReceiver receiverTest = dao.getReceiver(numTest);

        //Then
        assertNotNull(receiverTest);
        assertEquals(receiverActual.getNum(), receiverTest.getNum());
        assertEquals(receiverActual.getName(), receiverTest.getName());
    }

    @Test
    public void testAddExpense() throws SQLException {
        //Given
        DtoExpense expenseActual = new DtoExpense();
        int numTest = 7;
        expenseActual.setNum(numTest);
        expenseActual.setPaydate(Date.valueOf("2020-02-01"));
        expenseActual.setReceiver(3);
        expenseActual.setValue(1137.3);
        dao.addExpense(expenseActual);

        //When
        DtoExpense expenseTest = dao.getExpense(numTest);

        //Then
        assertNotNull(expenseTest);
        assertEquals(expenseTest.getNum(), expenseTest.getNum());
        assertEquals(expenseTest.getValue(), expenseTest.getValue(), 0);
        assertEquals(expenseTest.getReceiver(), expenseTest.getReceiver());
        assertEquals(expenseTest.getPaydate(), expenseTest.getPaydate());
    }

    @Before
    public void setUp() throws Exception {
        truncate();
        try {
            dao = (DaoImpl) DaoFactory.getDaoImpl("mysql_homework_test");
            connection = new MySqlConnection(MySqlDataSource.getTestConnection(), "mysql_homework_test");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        truncate();
        connection.close();
        dao = null;
    }

    private void truncate() throws SQLException {
        Connection connection = MySqlDataSource.getTestConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("truncate expenses");
        statement.executeUpdate("truncate receivers");
    }
}