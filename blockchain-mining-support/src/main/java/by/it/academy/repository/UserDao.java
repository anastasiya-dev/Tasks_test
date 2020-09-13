package by.it.academy.repository;

import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

//@Repository
public class UserDao implements BaseDao<User>
//        , ApplicationContextAware
{

    //    @Autowired
    SessionFactory sessionFactory;

//    private ApplicationContext context;

    @Override
//    @Transactional
    public void create(SessionFactory sessionFactory, User user) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User findById(SessionFactory sessionFactory, String id) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User u where u.userId=:userId", User.class);
        query.setParameter("userId", id);
        List<User> list = query.list();
        User user = null;
        try {
            user = list.get(0);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        session.close();
        return user;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User findByName(SessionFactory sessionFactory, String name) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User u where u.userName=:userName", User.class);
        query.setParameter("userName", name);
        List<User> list = query.list();
        User user = null;
        try {
            user = list.get(0);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        session.close();
        return user;
    }

    @Override
//    @Transactional(readOnly = true)
    public List<User> findAll(SessionFactory sessionFactory, String searchStr) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User u", User.class);
        List<User> list = query.list();
        session.close();
        return list;
    }

    @Override
    public List<User> findAllWithParameter(SessionFactory sessionFactory, String searchStr) {
        return null;
    }

    @Override
//    @Transactional
    public User update(SessionFactory sessionFactory, User user) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return user;
    }

    @Override
//    @Transactional
    public boolean delete(SessionFactory sessionFactory, User user) {
        return false;
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context = applicationContext;
//    }
}
