package by.it.academy.repository;

import java.util.List;

public interface BaseDao<T> {
    void create(T t);

    T find(String id);

    List<T> findAll(String searchStr);

    void update(T t);

    void delete(T t);
}
