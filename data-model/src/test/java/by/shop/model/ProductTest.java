package by.shop.model;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class ProductTest {

    SessionFactory factory;


    @Before
    public void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.test.cfg.xml") // configures settings from hibernate.cfg.xml
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    public void create() {

        //given:

        Product product = new Product();
//        product.id = 1;
        product.name = "Lenovo Notebook";
        product.productNumber = "1234asdf";
        product.serialNumber = "1234-6768";
        product.producedDate = new Date();

        //when:
        Session sess = factory.openSession();
        Transaction tx = null;
        String productId;
        try {
            tx = sess.beginTransaction();

            //do some work
            productId = (String) sess.save(product);
            product.serialNumber = "8888";
            sess.saveOrUpdate(product);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            sess.close();
        }

        //then
        assertNotNull(productId);
        assertTrue(productId.length() > 1);
    }

    @Test
    public void read() throws DatabaseUnitException, SQLException {
        //Given:

        IDatabaseConnection connection = new MySqlConnection(MySQLDataSource.getTestConnection(), "shop_test");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(ProductTest.class
                .getResourceAsStream("ProductTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        Session appSession = factory.openSession();

        //When

        Product product = null;
        Transaction tx = null;
        try {
            tx = appSession.beginTransaction();
            product = appSession.get(Product.class, "537dd9ab-35f5-42bb-b985-0e16d0f00000");
            System.out.println("Product name = " + product.name);
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            appSession.close();
        }

        //Then
        assertNotNull(product);
        assertEquals("Lenovo Notebook", product.name);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void update() throws DatabaseUnitException, SQLException {
        //Given:

        IDatabaseConnection connection = new MySqlConnection(MySQLDataSource.getTestConnection(), "shop_test");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(ProductTest.class
                .getResourceAsStream("ProductTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        Session appSession = factory.openSession();

        //When

        Product product = null;
        Transaction tx = null;
        try {
            tx = appSession.beginTransaction();
            product = appSession.get(Product.class, "537dd9ab-35f5-42bb-b985-0e16d0f00001");
            product.setProductNumber("updated number");
            product.setSerialNumber("updated serial");
            appSession.flush();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            appSession.close();
        }

        //then
        //manually check updated in workbench
        //we need to run the test separately - idea doesn't run them in the order as in java code
        //we can use junit annotations to manage the order
        connection.close();
    }

    @Test
    public void delete() throws DatabaseUnitException, SQLException {
        //Given:

        IDatabaseConnection connection = new MySqlConnection(MySQLDataSource.getTestConnection(), "shop_test");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(ProductTest.class
                .getResourceAsStream("ProductTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        Session appSession = factory.openSession();

        //When

        Product product1 = null;
        Product product2 = null;
        Transaction tx = null;
        try {
            tx = appSession.beginTransaction();
            product1 = appSession.get(Product.class, "537dd9ab-35f5-42bb-b985-0e16d0f00001");
            appSession.delete(product1);
            product2 = appSession.get(Product.class, "537dd9ab-35f5-42bb-b985-0e16d0f00000");
            appSession.delete(product2);

            appSession.flush();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            appSession.close();
        }

        //then
        //manually check updated in workbench
        //we need to run the test separately - idea doesn't run them in the order as in java code
        //we can use junit annotations to manage the order
        connection.close();
    }

    @Test
    public void query() throws DatabaseUnitException, SQLException {
        //Given:

        IDatabaseConnection connection = new MySqlConnection(MySQLDataSource.getTestConnection(), "shop_test");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(ProductTest.class
                .getResourceAsStream("ProductTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        Session session = factory.openSession();
        Query query = session.createQuery("from Product p where p.name like :product", Product.class);
        query.setParameter("product", "%Notebook%");
        query.setFirstResult(0);
        query.setMaxResults(2);
        List<Product> products = query.list();

        //Then
        assertNotNull(products);
        assertTrue(products.size() > 0);
        assertEquals(2, products.size());
        DatabaseOperation.DELETE.execute(connection, dataSet);
        connection.close();
        if (session.isOpen()) session.close();
    }

    @Test
    public void searchByDate() throws DatabaseUnitException, SQLException, ParseException {
        //Given:

        IDatabaseConnection connection = new MySqlConnection(MySQLDataSource.getTestConnection(), "shop_test");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(ProductTest.class
                .getResourceAsStream("ProductTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        Session session = factory.openSession();

        Criteria criteria = session.createCriteria(Product.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = sdf.parse("01-01-2020");
        Date endDate = sdf.parse("01-03-2020");
        criteria.add(Restrictions.between("producedDate", startDate, endDate));
        List<Product> list = criteria.list();

        //Then
        assertNotNull(list);
        assertEquals(2, list.size());
        DatabaseOperation.DELETE.execute(connection, dataSet);
        connection.close();
        if (session.isOpen()) session.close();

    }
}