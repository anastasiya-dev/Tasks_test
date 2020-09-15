package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    @Value("#{userDao}")
    BaseDao userDao;

    public boolean createNewUser(User user) {
        if (user.getUserId() != null) {
            if (userDao.findById(user.getUserId()) != null) {
                return false;
            }
        }
        userDao.create(user);
        return true;
    }

    public User findUserById(String id) {
        return (User) userDao.findById(id);
    }

    public User findUserByName(String name) {
        ArrayList<User> users = (ArrayList<User>) userDao.findAll("");
        for (User user : users) {
            if (user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public User update(User user) {
        return (User) userDao.update(user);
    }
}
