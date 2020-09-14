package by.it.academy.repository;

import by.it.academy.pojo.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class TransactionDao implements BaseDao<Transaction> {

    @Override
    public Transaction create(SessionFactory sessionFactory, Transaction transaction) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(transaction);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return transaction;
    }

    @Override
    public Transaction findById(SessionFactory sessionFactory, String id) {
        Session session = sessionFactory.openSession();
        Query<Transaction> query = session.createQuery("from Transaction t where t.transactionId=:transactionId", Transaction.class);
        query.setParameter("transactionId", id);
        List<Transaction> list = query.list();
        Transaction transaction = null;
        try {
            transaction = list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return transaction;
    }

    @Override
    public List<Transaction> findAll(SessionFactory sessionFactory, String searchStr) {
        Session session = sessionFactory.openSession();
        Query<Transaction> query = session.createQuery("from Transaction t", Transaction.class);
        List<Transaction> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Transaction update(SessionFactory sessionFactory, Transaction transaction) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        Query query = session.createQuery("update Transaction t set t.signature=:signature, t.transactionDateTime=:transactionDateTime where t.transactionId=:transactionId");
        query.setParameter("transactionId", transaction.getTransactionId());
        query.setParameter("signature", transaction.getSignature());
        query.setParameter("transactionDateTime", transaction.getTransactionDateTime());
        int result = query.executeUpdate();
        tx.commit();
        session.close();
        return transaction;
    }

    @Override
    public boolean delete(SessionFactory sessionFactory, Transaction transaction) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();

        if (transaction != null) {
            try {
                tx = session.beginTransaction();
                session.delete(transaction);
                tx.commit();
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
}
