package by.it.academy.repository;

import by.it.academy.pojo.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TransactionDao implements BaseDao<Transaction>, ApplicationContextAware {

    @Autowired
    SessionFactory sessionFactory;

    private ApplicationContext context;

    @Override
    @Transactional
    public void create(Transaction transaction) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(transaction);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Transaction findById(String id) {
        Session session = sessionFactory.openSession();
        Query<Transaction> query = session.createQuery("from Transaction t where t.transactionId=:transactionId", Transaction.class);
        query.setParameter("transactionId", id);
        List<Transaction> list = query.list();
        Transaction transaction = null;
        try {
            transaction = list.get(0);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        session.close();
        return transaction;
    }

    @Override
    public Transaction findByName(String id) {
        return null;
    }

    @Override
    public List<Transaction> findAll(String searchStr) {
        Session session = sessionFactory.openSession();
        Query<Transaction> query = session.createQuery("from Transaction", Transaction.class);
        List<Transaction> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Transaction update(Transaction transaction) {
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Transaction transaction) {
        System.out.println("in the delete method");
        org.hibernate.Transaction tx = null;
//        Transaction transactionToDelete = findById(transaction.getTransactionId());
        System.out.println(transaction);
        Session session = sessionFactory.openSession();

        if (transaction != null) {
            try {
                tx = session.beginTransaction();
                session.delete(transaction);
                tx.commit();
//                session.flush();
                return true;
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            } finally {
                session.close();
            }
        } else {
            return false;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
