//package by.it.academy.repository;
//
//import by.it.academy.pojo.TransactionOutput;
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
//public class TransactionOutputDao implements BaseDao<TransactionOutput>, ApplicationContextAware {
//
//    @Autowired
//    SessionFactory sessionFactory;
//
//    private ApplicationContext context;
//
//    @Override
//    public void create(TransactionOutput transactionOutput) {
//
//    }
//
//    @Override
//    public TransactionOutput findById(String id) {
//        return null;
//    }
//
//    @Override
//    public TransactionOutput findByName(String id) {
//        return null;
//    }
//
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//    public List<TransactionOutput> findAll(String searchStr) {
//        Session session = sessionFactory.openSession();
//        Query<TransactionOutput> query = session.createQuery("from TransactionOutput", TransactionOutput.class);
//        List<TransactionOutput> list = query.list();
//        session.close();
//        return list;
//    }
//
//    @Override
//    public List<TransactionOutput> findAllWithParameter(String searchStr) {
//        return null;
//    }
//
//    @Override
//    public TransactionOutput update(TransactionOutput transactionOutput) {
//        return null;
//    }
//
//    @Override
//    public boolean delete(TransactionOutput transactionOutput) {
//        return false;
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context = applicationContext;
//    }
//}
