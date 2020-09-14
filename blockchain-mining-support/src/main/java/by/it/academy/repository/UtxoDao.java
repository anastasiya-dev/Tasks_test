package by.it.academy.repository;

import by.it.academy.pojo.Utxo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class UtxoDao implements BaseDao<Utxo> {

    @Override

    public Utxo create(SessionFactory sessionFactory, Utxo utxo) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(utxo);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return utxo;
    }

    @Override
    public Utxo findById(SessionFactory sessionFactory, String id) {
        Session session = sessionFactory.openSession();
        Query<Utxo> query = session.createQuery("from Utxo bu where bu.utxoId=:utxoId", Utxo.class);
        query.setParameter("utxoId", id);
        List<Utxo> list = query.list();
        Utxo utxo = null;
        try {
            utxo = list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return utxo;
    }

    @Override
    public List<Utxo> findAll(SessionFactory sessionFactory, String searchStr) {
        Session session = sessionFactory.openSession();
        Query<Utxo> query = session.createQuery("from Utxo", Utxo.class);
        List<Utxo> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Utxo update(SessionFactory sessionFactory, Utxo utxo) {
        return null;
    }

    @Override
    public boolean delete(SessionFactory sessionFactory, Utxo utxo) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        if (utxo != null) {
            try {
                tx = session.beginTransaction();
                session.delete(utxo);
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
