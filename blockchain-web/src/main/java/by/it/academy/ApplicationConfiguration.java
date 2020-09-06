package by.it.academy;

import by.it.academy.pojo.*;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.security.Security;
import java.util.Properties;

@Configuration
@ComponentScan("by.it.academy")
@EnableTransactionManagement
@EnableWebMvc
public class ApplicationConfiguration {

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new org.apache.commons.dbcp.BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/blockchain?serverTimezone=UTC&createDatabaseIfNotExist=true");
        dataSource.setUsername("root");
        dataSource.setPassword("Atme3816liveon!");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setAnnotatedClasses(
                User.class,
                Block.class,
                Transaction.class,
                TransactionInput.class,
                TransactionOutput.class,
                Wallet.class);
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager hibernateTransactionManager =
                new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory);
        return hibernateTransactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("show_sql", "true");
        return properties;
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver
                = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5_000_000);
        return multipartResolver;
    }
}
