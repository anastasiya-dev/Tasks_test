package by.it.academy.pojo.repository;

import org.hibernate.SessionFactory;

import java.util.List;

public interface BaseDao<T> {
    void create(T t, SessionFactory sessionFactory);

    T findById(String id, SessionFactory sessionFactory);

    T findByName(String id, SessionFactory sessionFactory);

    List<T> findAll(String searchStr, SessionFactory sessionFactory);

    List<T> findAllWithParameter(String searchStr, SessionFactory sessionFactory);

    T update(T t, SessionFactory sessionFactory);

    boolean delete(T t, SessionFactory sessionFactory);
}
