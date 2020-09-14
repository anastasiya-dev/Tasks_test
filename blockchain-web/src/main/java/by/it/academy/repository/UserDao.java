package by.it.academy.repository;

import by.it.academy.pojo.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
public class UserDao implements BaseDao<User>, ApplicationContextAware {

    @Autowired
    SessionFactory sessionFactory;

    private ApplicationContext context;

    @Override
    @Transactional
    public User create(User user) {
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User findById(String id) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User u where u.userId=:userId", User.class);
        query.setParameter("userId", id);
        List<User> list = query.list();
        User user = null;
        try {
            user = list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(String searchStr) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User", User.class);
        List<User> list = query.list();
        session.close();
        return list;
    }

    @Override
    @Transactional
    public User update(User user) {
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
    @Transactional
    public boolean delete(User user) {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
