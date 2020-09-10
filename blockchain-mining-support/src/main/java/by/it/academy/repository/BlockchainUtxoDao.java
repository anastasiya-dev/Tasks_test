package by.it.academy.repository;

import by.it.academy.pojo.BlockchainUtxo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BlockchainUtxoDao implements BaseDao<BlockchainUtxo> {

    @Override

    public void create(SessionFactory sessionFactory, BlockchainUtxo blockchainUtxo) {
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
    public BlockchainUtxo findById(SessionFactory sessionFactory, String id) {
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
    public BlockchainUtxo findByName(SessionFactory sessionFactory, String id) {
        return null;
    }

    @Override
    public List<BlockchainUtxo> findAll(SessionFactory sessionFactory, String searchStr) {
        Session session = sessionFactory.openSession();
        Query<BlockchainUtxo> query = session.createQuery("from BlockchainUtxo", BlockchainUtxo.class);
        List<BlockchainUtxo> list = query.list();
        session.close();
        return list;
    }


    @Override
    public List<BlockchainUtxo> findAllWithParameter(SessionFactory sessionFactory, String searchStr) {
        return null;
    }

    @Override
    public BlockchainUtxo update(SessionFactory sessionFactory, BlockchainUtxo blockchainUtxo) {
        return null;
    }

    @Override
    public boolean delete(SessionFactory sessionFactory, BlockchainUtxo blockchainUtxo) {
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

}
