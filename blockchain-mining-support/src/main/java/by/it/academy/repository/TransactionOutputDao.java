package by.it.academy.repository;

import by.it.academy.pojo.TransactionOutput;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

//@Repository
public class TransactionOutputDao implements BaseDao<TransactionOutput> {

//    @Autowired
//    SessionFactory sessionFactory;
//
//    private ApplicationContext context;

    @Override
    public void create(SessionFactory sessionFactory, TransactionOutput transactionOutput) {
        org.hibernate.Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(transactionOutput);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public TransactionOutput findById(SessionFactory sessionFactory, String id) {
        return null;
    }

    @Override
    public TransactionOutput findByName(SessionFactory sessionFactory, String id) {
        return null;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<TransactionOutput> findAll(SessionFactory sessionFactory, String searchStr) {
        Session session = sessionFactory.openSession();
        Query<TransactionOutput> query = session.createQuery("from TransactionOutput", TransactionOutput.class);
        List<TransactionOutput> list = query.list();
        session.close();
        return list;
    }

    @Override
    public List<TransactionOutput> findAllWithParameter(SessionFactory sessionFactory, String searchStr) {
        return null;
    }

    @Override
    public TransactionOutput update(SessionFactory sessionFactory, TransactionOutput transactionOutput) {
        return null;
    }

    @Override
    public boolean delete(SessionFactory sessionFactory, TransactionOutput transactionOutput) {
        return false;
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context = applicationContext;
//    }
}
