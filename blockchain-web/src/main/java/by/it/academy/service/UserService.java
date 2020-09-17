package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.BaseDao;
import by.it.academy.repository.UserDao;
import by.it.academy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
//    @Value("#{userDao}")
//    BaseDao userDao;
    UserRepository userRepository;

    @Transactional
    public boolean createNewUser(User user) {
        if (user.getUserId() != null) {
            if (userRepository.findById(user.getUserId()) != null) {
                return false;
            }
        }
        userRepository.save(user);
        return true;
    }

    public User findUserById(String id) {
        return (User) userRepository.findById(id).get();
    }

    public User findUserByName(String name) {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        for (User user : users) {
            if (user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }

//    public User update(User user) {
//        return (User) userRepository.update(user);
//    }
}
