package by.it.academy.repository;

import java.util.List;

public interface BaseDao<T> {
    void create(T t);

    T findById(String id);

    T findByName(String id);

    List<T> findAll(String searchStr);

    T update(T t);

    void delete(T t);
}
