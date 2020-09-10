package by.it.academy.repository;

import org.hibernate.SessionFactory;

import java.util.List;

public interface BaseDao<T> {
    void create(SessionFactory sessionFactory, T t);

    T findById(SessionFactory sessionFactory, String id);

    T findByName(SessionFactory sessionFactory, String id);

    List<T> findAll(SessionFactory sessionFactory, String searchStr);

    List<T> findAllWithParameter(SessionFactory sessionFactory, String searchStr);

    T update(SessionFactory sessionFactory, T t);

    boolean delete(SessionFactory sessionFactory, T t);
}
