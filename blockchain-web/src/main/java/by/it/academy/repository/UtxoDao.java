package by.it.academy.repository;

import by.it.academy.pojo.Utxo;
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
public class UtxoDao implements BaseDao<Utxo>, ApplicationContextAware {

    @Autowired
    SessionFactory sessionFactory;

    private ApplicationContext context;


    @Override
    @Transactional
    public Utxo create(Utxo utxo) {
        System.out.println("Saving utxo!");
        System.out.println(utxo);
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Utxo findById(String id) {
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Utxo> findAll(String searchStr) {
        Session session = sessionFactory.openSession();
        Query<Utxo> query = session.createQuery("from Utxo", Utxo.class);
        List<Utxo> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Utxo update(Utxo utxoFromChain) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        Query query = session.createQuery("update Utxo u set u.walletId=:walletId, u.outputTransactionId=:outputTransactionId where u.utxoId=:utxoId");
        query.setParameter("walletId", utxoFromChain.getWalletId());
        query.setParameter("outputTransactionId", utxoFromChain.getOutputTransactionId());
        query.setParameter("utxoId", utxoFromChain.getUtxoId());
        int result = query.executeUpdate();
        tx.commit();
        session.close();
        return utxoFromChain;
    }

    @Override
    @Transactional
    public boolean delete(Utxo utxo) {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
