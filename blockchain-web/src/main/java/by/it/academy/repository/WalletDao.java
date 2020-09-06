package by.it.academy.repository;

import by.it.academy.pojo.Wallet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class WalletDao implements BaseDao<Wallet>, ApplicationContextAware {

    @Autowired
    SessionFactory sessionFactory;

    private ApplicationContext context;

    @Override
    @Transactional
    public void create(Wallet wallet) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(wallet);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Wallet findById(String id) {
        return null;
    }

    @Override
    public Wallet findByName(String id) {
        return null;
    }

    @Override
    public List<Wallet> findAll(String searchStr) {
        return null;
    }

    @Override
    public Wallet update(Wallet wallet) {
        return null;
    }

    @Override
    public void delete(Wallet wallet) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
