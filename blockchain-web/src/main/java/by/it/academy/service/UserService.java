package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    @Value("#{userDao}")
    BaseDao baseDao;

    public boolean createNewUser(User user) {
        if (baseDao.findById(user.getUserId()) != null) {
            return false;
        }
        baseDao.create(user);
        return true;
    }

    public User findUserById(String id) {
        return (User) baseDao.findById(id);
    }

    public User findUserByName(String name) {
        return (User) baseDao.findByName(name);
    }

    public User update(User user) {
        return (User) baseDao.update(user);
    }
}
