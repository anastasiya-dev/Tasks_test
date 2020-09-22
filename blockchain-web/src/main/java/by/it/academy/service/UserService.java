package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.UserRepository;
import by.it.academy.support.ChangePassword;
import by.it.academy.support.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public boolean saveUser(User user) {
        if (user.getUserId() != null && userRepository.findById(user.getUserId()).isPresent()) {
            return false;
        }

        final String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(encodedPassword);

        user.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return true;
    }

    public User findUserById(String id) {
        return userRepository.findById(id).get();
    }

    public User findUserByName(String name, UserStatus userStatus) {

        User user = userRepository.findByUserNameAndUserStatus(name, userStatus);
        if (user == null || user.getUserStatus().equals(UserStatus.DELETED)) {
            System.out.println("No active users found");
            return null;
        } else {
            return userRepository.findByUserName(name);
        }
    }

    public User updateUser(User user) {
        User userSaved = userRepository.findById(user.getUserId()).get();
        userSaved.setUserName(user.getUserName());
        userSaved.setEmail(user.getEmail());
        userSaved.setMobile(user.getMobile());
        userRepository.save(userSaved);
        return userRepository.findById(userSaved.getUserId()).get();
    }

    public User updatePassword(String userId, ChangePassword changePassword) {
        User userSaved = userRepository.findById(userId).get();
        userSaved.setUserPassword(changePassword.getNewPassword());
        userSaved.setConfirmPassword(changePassword.getConfirmPassword());
        userRepository.save(userSaved);
        return userRepository.findById(userId).get();
    }


    public User delete(User user) {
        User userSaved = userRepository.findById(user.getUserId()).get();
        userSaved.setUserStatus(user.getUserStatus());
        userRepository.save(user);
        return userRepository.findById(userSaved.getUserId()).get();
    }
}
