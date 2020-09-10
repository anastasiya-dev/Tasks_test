package by.it.academy.pojo.repository;

import by.it.academy.pojo.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class TransactionDao implements BaseDao<Transaction> {

    @Override
    public void create(Transaction transaction, SessionFactory sessionFactory) {
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
    }

    @Override
    public Transaction findById(String id, SessionFactory sessionFactory) {
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
    public Transaction findByName(String id, SessionFactory sessionFactory) {
        return null;
    }

    @Override
    public List<Transaction> findAll(String searchStr, SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        Query<Transaction> query = session.createQuery("from Transaction", Transaction.class);
        List<Transaction> list = query.list();
        session.close();
        return list;
    }

    @Override
    public List<Transaction> findAllWithParameter(String searchStr, SessionFactory sessionFactory) {
//        Session session = sessionFactory.openSession();
//        Wallet wallet = (Wallet) walletDao.findById(searchStr);
//        Query<Transaction> query = session.createQuery("from Transaction t where t.recipient=:recipient", Transaction.class);
//        query.setParameter("recipient", wallet.publicKey);
////        query.setParameter("sender", wallet.publicKey);
//        List<Transaction> list = query.list();
//        session.close();
        return null;
    }

    @Override
    public Transaction update(Transaction transaction, SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        Query query = session.createQuery("update Transaction t set t.signature=:signature, t.transactionDateTime=:transactionDateTime where t.transactionId=:transactionId");
        query.setParameter("transactionId", transaction.getTransactionId());
        query.setParameter("signature", transaction.getSignature());
        query.setParameter("transactionDateTime", transaction.getTransactionDateTime());
//        query.setParameter("outputs", transaction.getOutputs());
        int result = query.executeUpdate();
        tx.commit();
        session.close();
        return transaction;
    }

    @Override
    public boolean delete(Transaction transaction, SessionFactory sessionFactory) {
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

}
