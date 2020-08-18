package by.shop.model;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;

public class ModelTest {

    SessionFactory factory;
    StandardServiceRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.test.cfg.xml")
                .build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @After
    public void tearDown() throws Exception {
        if (!factory.isClosed()) {
            factory.close();
            factory = null;
        }
    }
}
