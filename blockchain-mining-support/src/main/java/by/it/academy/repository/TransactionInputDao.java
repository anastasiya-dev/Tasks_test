//package by.it.academy.repository;
//
//import by.it.academy.pojo.TransactionInput;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//
//import java.util.List;
//
////@Repository
//public class TransactionInputDao implements BaseDao<TransactionInput> {
//
//
//    //    @Autowired
//    SessionFactory sessionFactory;
//
////    private ApplicationContext context;
//
//    @Override
//    public void create(SessionFactory sessionFactory, TransactionInput transactionInput) {
//        org.hibernate.Transaction tx = null;
//        Session session = sessionFactory.openSession();
//        try {
//            tx = session.beginTransaction();
//            session.saveOrUpdate(transactionInput);
//            tx.commit();
//        } catch (Exception e) {
//            if (tx != null) tx.rollback();
//            throw e;
//        } finally {
//            session.close();
//        }
//    }
//
//    @Override
////    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//    public TransactionInput findById(SessionFactory sessionFactory, String id) {
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
//    public TransactionInput findByName(SessionFactory sessionFactory, String id) {
//        return null;
//    }
//
//    @Override
//    public List<TransactionInput> findAll(SessionFactory sessionFactory, String searchStr) {
//        return null;
//    }
//
//    @Override
//    public List<TransactionInput> findAllWithParameter(SessionFactory sessionFactory, String searchStr) {
//        return null;
//    }
//
//    @Override
//    public TransactionInput update(SessionFactory sessionFactory, TransactionInput transactionInput) {
//        return null;
//    }
//
//    @Override
//    public boolean delete(SessionFactory sessionFactory, TransactionInput transactionInput) {
//        return false;
//    }
//
////    @Override
////    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
////        context = applicationContext;
////    }
//}
