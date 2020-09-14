//package by.it.academy.repository;
//
//import by.it.academy.pojo.TransactionInput;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public class TransactionInputDao implements BaseDao<TransactionInput>, ApplicationContextAware {
//
//
//    @Autowired
//    SessionFactory sessionFactory;
//
//    private ApplicationContext context;
//
//    @Override
//    public void create(TransactionInput transactionInput) {
//
//    }
//
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//    public TransactionInput findById(String id) {
//        Session session = sessionFactory.openSession();
//        Query<TransactionInput> query = session.createQuery("from TransactionInput ti where ti.transactionOutputId=:transactionOutputId", TransactionInput.class);
//        query.setParameter("transactionOutputId", id);
//        List<TransactionInput> list = query.list();
//        TransactionInput transactionInput = null;
//        try {
//            transactionInput = list.get(0);
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
//        session.close();
//        return transactionInput;
//    }
//
//    @Override
//    public TransactionInput findByName(String id) {
//        return null;
//    }
//
//    @Override
//    public List<TransactionInput> findAll(String searchStr) {
//        return null;
//    }
//
//    @Override
//    public List<TransactionInput> findAllWithParameter(String searchStr) {
//        return null;
//    }
//
//    @Override
//    public TransactionInput update(TransactionInput transactionInput) {
//        return null;
//    }
//
//    @Override
//    public boolean delete(TransactionInput transactionInput) {
//        return false;
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context = applicationContext;
//    }
//}
