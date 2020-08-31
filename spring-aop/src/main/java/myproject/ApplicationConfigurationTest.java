package myproject;

import myproject.hibernate.TransferObject;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("myproject")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class ApplicationConfigurationTest implements BeanPostProcessor {

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TransferObject tdo() {
        return new TransferObject();
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new org.apache.commons.dbcp.BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_hib_hw_test?serverTimezone=UTC&createDatabaseIfNotExist=true");
        dataSource.setUsername("root");
        dataSource.setPassword("Atme3816liveon!");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean
    //we expect that spring will find dataSource object itself
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setAnnotatedClasses(TransferObject.class);
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }


    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("show_sql", "true");
        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory);
        return hibernateTransactionManager;
    }
}
