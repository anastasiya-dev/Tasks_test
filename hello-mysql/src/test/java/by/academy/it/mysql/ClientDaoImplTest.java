package by.academy.it.mysql;

import by.academy.it.ClientDaoFactory;
import by.academy.it.ClientDto;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ClientDaoImplTest {

    private static ClientDaoImpl clientDao = null;
    //db unit
    IDatabaseConnection connection;

    @org.junit.Before
    public void setUp() throws Exception {
        truncate();
        try {
            clientDao = (ClientDaoImpl) ClientDaoFactory.getClientDao("mysql_test");
            //db unit
            connection = new MySqlConnection(MySqlDataSource.getTestConnection(), "client_test");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.After
    public void tearDown() throws Exception {
        truncate();
        //db unit
        connection.close();
        //for garbage collector
        clientDao = null;
    }

    public void testLines() throws Exception {
        ClientDto clientDto_test_1 = new ClientDto();
        clientDto_test_1.setId(201);
        clientDto_test_1.setName("test_Name_1");
        clientDto_test_1.setSecondName("test_Second_Name_1");
        clientDto_test_1.setEmail("test_email_1");
        clientDto_test_1.setDateOfBirth(Date.valueOf("2001-01-01"));
        clientDto_test_1.setGender('F');

        ClientDto clientDto_test_2 = new ClientDto();
        clientDto_test_2.setId(202);
        clientDto_test_2.setName("test_Name_2");
        clientDto_test_2.setSecondName("test_Second_Name_2");
        clientDto_test_2.setEmail("test_email_2");
        clientDto_test_2.setDateOfBirth(Date.valueOf("2002-01-01"));
        clientDto_test_2.setGender('F');
        clientDao.create(clientDto_test_1);
        clientDao.create(clientDto_test_2);
    }


    public void truncate() throws Exception {
        Connection connection = MySqlDataSource.getTestConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("truncate clients");
        System.out.println("Truncating db to start the test");
    }

    @org.junit.Test(expected = Exception.class)
    public void create_NotNullId() throws Exception {
        truncate();
        testLines();
        ClientDto clientDto_test = new ClientDto();
        Object idObject = null;
        clientDto_test.setId((int) idObject);
    }

    @org.junit.Test(expected = Exception.class)
    public void create_NoDuplicateId() throws Exception {
        truncate();
        testLines();
        ClientDto clientDto_test = new ClientDto();
        Random rnd = new Random();
        clientDto_test.setId(rnd.nextInt(clientDao.getMaxId()));
        for (int i = 0; i < clientDao.getMaxId(); i++) {
            if (clientDto_test.getId() == i) {
                clientDao.create(clientDto_test);
            } else throw new Exception();
        }
    }

    @org.junit.Test
    public void read_NotNullDatabase() throws Exception {
        truncate();
        testLines();
        assertTrue(clientDao != null);
    }

    @org.junit.Test
    public void readAll() throws Exception {
        truncate();
        testLines();
    }

    @org.junit.Test
    public void update() throws Exception {
        truncate();
        testLines();
    }

    @Ignore
    @Test
    public void deleteAll_completenessOfDeletion() throws Exception {
        truncate();
        testLines();
        try {
            List<ClientDto> rl = clientDao.readAll();
            for (int i = 0; i < rl.size(); i++) {
                clientDao.delete(rl.get(i));
            }
            List<ClientDto> rl2 = clientDao.readAll();
            for (int i = 0; i < rl2.size(); i++) {
                System.out.println(rl2.get(i).getId());
            }
            assertNull(clientDao);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @org.junit.Test
//    public void delete_CheckItRemoved() throws Exception {
//        truncate();
//        testLines();
//        List<ClientDto> rlBefore = clientDao.readAll();
//        Random rnd = new Random();
//        int numberDel = rnd.nextInt(rlBefore.size());
//        int idDel = rlBefore.get(numberDel).getId();
//        clientDao.delete(idDel);
//        List<ClientDto> rlAfter = clientDao.readAll();
//
//    }

    @org.junit.Test
    public void delete_CheckOtherRemained() throws Exception {
        truncate();
        testLines();
    }

    @org.junit.Test
    public void getMaxId_CheckMaximum() throws Exception {
        truncate();
        testLines();
        int maxFromMethod = clientDao.getMaxId();
        boolean check = true;
        List<ClientDto> rl = clientDao.readAll();
        for (ClientDto clientDto : rl) {
            if (clientDto.getId() > maxFromMethod) {
                check = false;
                break;
            }
        }
        assertTrue(check);
    }

    @org.junit.Test
    public void getMaxId() throws SQLException, DatabaseUnitException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(ClientDaoImplTest.class.getResourceAsStream("ClientDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        int maxId = clientDao.getMaxId();
        ClientDto clientDto = clientDao.read(2);

        //Then
        assertNotNull(clientDto);
        assertEquals(maxId, 5);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }
}