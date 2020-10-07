package by.it.academy.service;

import by.it.academy.pojo.Approle;
import by.it.academy.pojo.User;
import by.it.academy.pojo.UserApprole;
import by.it.academy.repository.ApproleRepository;
import by.it.academy.repository.UserApproleRepository;
import by.it.academy.repository.UserRepository;
import by.it.academy.support.UserStatus;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    static int sequence = 0;

    @Autowired
    UserRepository userRepository;
    @Autowired
    User user;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ApproleRepository approleRepository;
    @Autowired
    UserApproleRepository userApproleRepository;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(UserService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public User createAndSaveUser(String userName, String userPassword) {

        user.setUserId(String.valueOf(sequence));
        user.setUserName(userName);
        String encodedPassword = passwordEncoder.encode(userPassword);
        user.setUserPassword(encodedPassword);
        user.setUserStatus(UserStatus.ACTIVE);
        sequence++;
        logger.info("Creating and saving user: " + user);
        return userRepository.save(user);
    }

    public void appRoleAssignment(String userId, String roleName) {
        Approle approle = new Approle();
        approle.setRoleName(roleName);
        Approle savedApprole = approleRepository.save(approle);
        UserApprole userApprole = new UserApprole();
        userApprole.setUserUserId(userId);
        userApprole.setRolesId(savedApprole.getId());
        userApproleRepository.save(userApprole);
        logger.info("Performing app role assignment: " + userApprole);
    }

    public User findUserById(String id) {
        logger.info("Extracting user by id: " + id);
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll(UserStatus userStatus) {
        logger.info("Extracting all users by status: " + userStatus);
        List<User> allUsers = (List<User>) userRepository.findAll();
        List<User> activeUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getUserStatus().equals(UserStatus.ACTIVE)) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }

    public boolean findUserByNameExistence(String name) {
        logger.info("Checking user existence by name: " + name);
        if (userRepository.findByUserName(name) != null) {
            logger.info("Found");
            return true;
        } else {
            logger.info("Not found");
            return false;
        }
    }

    public User findUserByName(String name) {
        logger.info("Extracting user by name: " + name);
        return userRepository.findByUserName(name);
    }
}

