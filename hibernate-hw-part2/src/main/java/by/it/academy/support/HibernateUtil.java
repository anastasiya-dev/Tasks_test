package by.it.academy.support;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    static SessionFactory factory;
    static StandardServiceRegistry registry;

    public static SessionFactory start() throws Exception {

        registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        return factory;
    }

    public static void finish() throws Exception {
        if (!factory.isClosed()) {
            factory.close();
            factory = null;
        }
    }
}
