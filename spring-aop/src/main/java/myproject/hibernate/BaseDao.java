package myproject.hibernate;

import java.io.Serializable;

public interface BaseDao<T> {
    String create(T t);

    T read (Class clazz, Serializable id);

    int update(String string);

    int delete(T t);
}
