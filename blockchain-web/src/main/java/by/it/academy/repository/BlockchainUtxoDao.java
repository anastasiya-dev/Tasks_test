package by.it.academy.repository;

import by.it.academy.pojo.BlockchainUtxo;
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
public class BlockchainUtxoDao implements BaseDao<BlockchainUtxo>, ApplicationContextAware {

    @Autowired
    SessionFactory sessionFactory;

    private ApplicationContext context;


    @Override
    @Transactional
    public void create(BlockchainUtxo blockchainUtxo) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(blockchainUtxo);
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
    public BlockchainUtxo findById(String id) {
        Session session = sessionFactory.openSession();
        Query<BlockchainUtxo> query = session.createQuery("from BlockchainUtxo bu where bu.blockchainUtxoId=:blockchainUtxoId", BlockchainUtxo.class);
        query.setParameter("blockchainUtxoId", id);
        List<BlockchainUtxo> list = query.list();
        BlockchainUtxo blockchainUtxo = null;
        try {
            blockchainUtxo = list.get(0);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        session.close();
        return blockchainUtxo;
    }

    @Override
    public BlockchainUtxo findByName(String id) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<BlockchainUtxo> findAll(String searchStr) {
        Session session = sessionFactory.openSession();
        Query<BlockchainUtxo> query = session.createQuery("from BlockchainUtxo", BlockchainUtxo.class);
        List<BlockchainUtxo> list = query.list();
        session.close();
        return list;
    }


    @Override
    public List<BlockchainUtxo> findAllWithParameter(String searchStr) {
        return null;
    }

    @Override
    public BlockchainUtxo update(BlockchainUtxo blockchainUtxo) {
        return null;
    }

    @Override
    @Transactional
    public boolean delete(BlockchainUtxo blockchainUtxo) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        if (blockchainUtxo != null) {
            try {
                tx = session.beginTransaction();
                session.delete(blockchainUtxo);
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
