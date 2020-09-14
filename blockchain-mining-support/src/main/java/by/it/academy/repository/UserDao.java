package by.it.academy.repository;

import by.it.academy.pojo.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDao implements BaseDao<User> {

    @Override
    public User create(SessionFactory sessionFactory, User user) {
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
    public User findById(SessionFactory sessionFactory, String id) {
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
    public List<User> findAll(SessionFactory sessionFactory, String searchStr) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User u", User.class);
        List<User> list = query.list();
        session.close();
        return list;
    }

    @Override
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
    public boolean delete(SessionFactory sessionFactory, User user) {
        return false;
    }
}
