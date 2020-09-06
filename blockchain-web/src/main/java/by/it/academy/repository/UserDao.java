package by.it.academy.repository;

import by.it.academy.pojo.User;
import org.hibernate.SessionFactory;
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
    public void create(User user) {
        sessionFactory.getCurrentSession()
                .saveOrUpdate(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User find(String id) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from User u where u.userId=:userId", User.class)
                .setParameter("userId", id)
                .list()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(String searchStr) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from User", User.class)
                .list();
    }

    @Override
    @Transactional
    public void update(User user) {
        sessionFactory.getCurrentSession()
                .update(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        sessionFactory.getCurrentSession()
                .delete(user);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
