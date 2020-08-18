package by.academy.it;

import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main_2 {

    private static Logger log = Logger.getLogger(Main_2.class.getName());

    public static void main(String[] args) {
        try {
            ClientDao clientDao = ClientDaoFactory.getClientDao("mysql");

        //Create
        //truncate client.clients - in workbench;
            ClientDto client = new ClientDto();
            client.setId(101);
            client.setName("Name");
            client.setSecondName("SecondName");
            client.setEmail("name@mail.ru");
            client.setDateOfBirth(Date.valueOf("1977-05-24"));
            client.setGender('F');
            log.info("Calling dao: " + clientDao);
            clientDao.create(client);

            //Read created
            ClientDto clientDtoCreated = clientDao.read(101);
            log.info("Read after created: " + clientDtoCreated);

            //Read maxID
            log.info("Max id in db: " + clientDao.getMaxId());

            //Update created
            clientDtoCreated.setName("Name 101");
            clientDtoCreated.setSecondName("Second Name 100");
            clientDao.update(clientDtoCreated);

            //Read updated
            ClientDto clientDtoUpdated = clientDao.read(101);
            log.info("Read after update: " + clientDtoCreated);

            //Delete updated
            clientDao.delete(clientDtoUpdated);
            log.info("Object deleted: " + clientDtoUpdated.getId());

        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            System.exit(-1);
        } finally {
            log.info("Finished successfully");
            System.exit(0);
        }
    }
}
