package by.it.academy;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

public class AnimalTest {

    static EntityManager entityManager;
    private static final Logger logger = LoggerFactory.logger(Animal.class);

    @BeforeClass
    public static void setUp() {

        entityManager = HibernateUtilTest.getEntityManager();
    }

    @Test
    public void create() {
        //Given
        Animal animal = new Animal();
        animal.setDateOfBirth(new Date());
        animal.setName("Sharik");
        animal.setNumberOfLegs(4);
        animal.setSex('F');

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(animal);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            entityManager.getTransaction().rollback();
        }

        //When
        Animal animalCreated = entityManager.find(Animal.class, animal.id);

        //Then
        assertNotNull(animalCreated);
        assertEquals(animal.id, animalCreated.id);
        assertEquals(animal.name, animalCreated.name);
        assertEquals(animal.numberOfLegs, animalCreated.numberOfLegs);
        assertEquals(animal.sex, animalCreated.sex);
        assertEquals(animal.dateOfBirth, animalCreated.dateOfBirth);
    }

    @Test
    public void read() throws DatabaseUnitException, SQLException {
        //Given

        IDatabaseConnection connection = new MySqlConnection(MySQLDataSource.getTestConnection(), "animals_test");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(AnimalTest.class
                .getResourceAsStream("AnimalTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        entityManager.getTransaction().begin();
        Animal animal1 = entityManager.find(Animal.class, "1");
        Animal animal2 = entityManager.find(Animal.class, "2");
        Animal animal3 = entityManager.find(Animal.class, "3");
        entityManager.getTransaction().commit();

        //Then
        assertNotNull(animal1);
        assertNotNull(animal2);
        assertNotNull(animal3);
        assertEquals(Timestamp.valueOf("2020-01-01 16:37:48.265000"), animal1.dateOfBirth);
        assertEquals(Timestamp.valueOf("2020-01-02 16:37:48.265000"), animal2.dateOfBirth);
        assertEquals(Timestamp.valueOf("2020-01-03 16:37:48.265000"), animal3.dateOfBirth);
        assertEquals("Barbos", animal1.name);
        assertEquals("Murka", animal2.name);
        assertEquals("Sharik", animal3.name);
        assertEquals(4, animal1.numberOfLegs);
        assertEquals(4, animal2.numberOfLegs);
        assertEquals(4, animal3.numberOfLegs);
        assertEquals('M', animal1.sex);
        assertEquals('F', animal2.sex);
        assertEquals('M', animal3.sex);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void update() {
        //Given
        Animal animal = new Animal();
        animal.setDateOfBirth(new Date());
        animal.setName("Sharik");
        animal.setNumberOfLegs(4);
        animal.setSex('F');

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(animal);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            entityManager.getTransaction().rollback();
        }

        //When
        Animal animalCreated = entityManager.find(Animal.class, animal.id);
        assertEquals("Sharik", animalCreated.name);

        entityManager.getTransaction().begin();
        animalCreated.setName("Pushok");
        entityManager.getTransaction().commit();
        Animal animalUpdated = entityManager.find(Animal.class, animal.id);

        //Then
        assertEquals("Pushok", animalUpdated.name);
    }

    @Test
    public void delete() throws DatabaseUnitException, SQLException {
        //Given
        Animal animal = new Animal();
        animal.setDateOfBirth(new Date());
        animal.setName("Sharik");
        animal.setNumberOfLegs(4);
        animal.setSex('F');

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(animal);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            entityManager.getTransaction().rollback();
        }

        //When
        Animal animalCreated = entityManager.find(Animal.class, animal.id);
        entityManager.getTransaction().begin();
        entityManager.remove(animalCreated);
        entityManager.getTransaction().commit();
        Animal animalDeleted = entityManager.find(Animal.class, animal.id);

        //Then
        assertNotNull(animalCreated);
        assertNull(animalDeleted);
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtilTest.close();
    }
}