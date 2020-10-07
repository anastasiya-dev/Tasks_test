package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.UserRepository;
import by.it.academy.support.ChangePassword;
import by.it.academy.support.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public boolean saveUser(User user) {
        log.info("Saving user: " + user);
        if (user.getUserId() != null && userRepository.findById(user.getUserId()).isPresent()) {
            log.warn("Denied. Already exists");
            return false;
        }

        final String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(encodedPassword);
        user.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        log.info("Accepted");
        return true;
    }

    public User findUserById(String id) {
        log.info("Extracting user from repository - by id: " + id);
        return userRepository.findById(id).get();
    }

    public User findUserByName(String name) {
        try {
            log.info("Extracting user from repository - by name: " + name);
            User user = userRepository.findByUserNameAndUserStatus(name, UserStatus.ACTIVE);
            if (user == null || user.getUserStatus().equals(UserStatus.DELETED)) {
                log.warn("Denied. No active users found");
                return null;
            } else {
                log.info("Accepted");
                return userRepository.findByUserName(name);
            }
        } catch (Exception e) {
            return userRepository
                    .findAll()
                    .stream()
                    .filter(u -> u.getUserStatus().equals(UserStatus.ACTIVE))
                    .filter(u -> u.getUserName().equals(name))
                    .findFirst()
                    .orElseThrow();
        }
    }

    public User updateUser(User user) {
        log.info("Updating user");
        User userSaved = userRepository.findById(user.getUserId()).get();
        log.info("Initial: " + userSaved);
        log.info("New: " + user);
        userSaved.setUserName(user.getUserName());
        userSaved.setEmail(user.getEmail());
        userSaved.setMobile(user.getMobile());
        userRepository.save(userSaved);
        return userRepository.findById(userSaved.getUserId()).get();
    }

    public User updatePassword(String userId, ChangePassword changePassword) {
        log.info("Updating password for user " + userId);
        User userSaved = userRepository.findById(userId).get();
        final String encodedPasswordNew = passwordEncoder.encode(changePassword.getNewPassword());
        userSaved.setUserPassword(encodedPasswordNew);
        final String confirmPasswordNew = passwordEncoder.encode(changePassword.getConfirmPassword());
        userSaved.setConfirmPassword(confirmPasswordNew);
        userRepository.save(userSaved);
        return userRepository.findById(userId).get();
    }


    public User delete(User user) {
        log.info("Deleting user: " + user);
        User userSaved = userRepository.findById(user.getUserId()).get();
        userSaved.setUserStatus(user.getUserStatus());
        userRepository.save(user);
        return userRepository.findById(userSaved.getUserId()).get();
    }

    public static String getUsernameAuthUser() {
        log.info("Getting user from authentication context");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();//get logged in username
    }
}
