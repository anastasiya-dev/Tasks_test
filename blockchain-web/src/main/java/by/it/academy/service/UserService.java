package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    BaseDao baseDao;

    public boolean createNewUser(User user) {
        if (baseDao.find(user.getUserId()) != null) {
            return false;
        }
        baseDao.create(user);
        return true;
    }

    public User get(String id) {
        return (User) baseDao.find(id);
    }
}
