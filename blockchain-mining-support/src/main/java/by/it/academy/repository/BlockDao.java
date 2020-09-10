package by.it.academy.repository;

import by.it.academy.pojo.Block;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class BlockDao implements BaseDao<Block>{
    @Override
    public void create(SessionFactory sessionFactory, Block block) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(block);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Block findById(SessionFactory sessionFactory, String id) {
        return null;
    }

    @Override
    public Block findByName(SessionFactory sessionFactory, String id) {
        return null;
    }

    @Override
    public List<Block> findAll(SessionFactory sessionFactory, String searchStr) {
        return null;
    }

    @Override
    public List<Block> findAllWithParameter(SessionFactory sessionFactory, String searchStr) {
        return null;
    }

    @Override
    public Block update(SessionFactory sessionFactory, Block block) {
        return null;
    }

    @Override
    public boolean delete(SessionFactory sessionFactory, Block block) {
        return false;
    }
}