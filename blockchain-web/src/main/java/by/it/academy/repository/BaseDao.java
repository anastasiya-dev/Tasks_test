package by.it.academy.repository;

import java.util.List;

public interface BaseDao<T> {
    T create(T t);

    T findById(String id);

    List<T> findAll(String searchStr);

    T update(T t);

    boolean delete(T t);
}
