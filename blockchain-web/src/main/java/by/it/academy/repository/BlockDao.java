//package by.it.academy.repository;
//
//import by.it.academy.pojo.Block;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class BlockDao implements BaseDao<Block>, ApplicationContextAware {
//
//    @Autowired
//    SessionFactory sessionFactory;
//
//    ApplicationContext context;
//
//    @Override
//    public Block create(Block block) {
//        org.hibernate.Transaction tx = null;
//        Session session = sessionFactory.openSession();
//        try {
//            tx = session.beginTransaction();
//            session.saveOrUpdate(block);
//            tx.commit();
//        } catch (Exception e) {
//            if (tx != null) tx.rollback();
//            throw e;
//        } finally {
//            session.close();
//        }
//        return block;
//    }
//
//    @Override
//    public Block findById(String id) {
//        return null;
//    }
//
//    @Override
//    public List<Block> findAll(String searchStr) {
//        return null;
//    }
//
//    @Override
//    public Block update(Block block) {
//        return null;
//    }
//
//    @Override
//    public boolean delete(Block block) {
//        return false;
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context = applicationContext;
//    }
//}
