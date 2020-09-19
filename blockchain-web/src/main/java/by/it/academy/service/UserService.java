package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean saveUser(User user) {
        if (user.getUserId() != null && userRepository.findById(user.getUserId()).isPresent()) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public User findUserById(String id) {
        return userRepository.findById(id).get();
    }

    public User findUserByName(String name) {
        return userRepository.findByUserName(name);
    }
}
