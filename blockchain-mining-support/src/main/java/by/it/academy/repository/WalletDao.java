package by.it.academy.repository;

import by.it.academy.pojo.Wallet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class WalletDao implements BaseDao<Wallet> {

    @Override
    public Wallet create(SessionFactory sessionFactory, Wallet wallet) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(wallet);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return wallet;
    }

    @Override
    public Wallet findById(SessionFactory sessionFactory, String id) {
        Session session = sessionFactory.openSession();
        Query<Wallet> query = session.createQuery("from Wallet w where w.walletId=:walletId", Wallet.class);
        query.setParameter("walletId", id);
        List<Wallet> list = query.list();
        Wallet wallet = null;
        try {
            wallet = list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return wallet;
    }

    @Override
    public List<Wallet> findAll(SessionFactory sessionFactory, String searchStr) {
        Session session = sessionFactory.openSession();
        Query<Wallet> query = session.createQuery("from Wallet w", Wallet.class);
        List<Wallet> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Wallet update(SessionFactory sessionFactory, Wallet wallet) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.update(wallet);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return wallet;
    }

    @Override
    public boolean delete(SessionFactory sessionFactory, Wallet wallet) {
        return false;
    }

}
