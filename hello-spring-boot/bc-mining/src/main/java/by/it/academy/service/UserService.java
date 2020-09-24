package by.it.academy.service;

import by.it.academy.pojo.Approle;
import by.it.academy.pojo.User;
import by.it.academy.pojo.UserApprole;
import by.it.academy.repository.ApproleRepository;
import by.it.academy.repository.UserApproleRepository;
import by.it.academy.repository.UserRepository;
import by.it.academy.support.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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


    public User createAndSaveUser(String userName, String userPassword) {
        user.setUserId(String.valueOf(sequence));
        user.setUserName(userName);
        String encodedPassword = passwordEncoder.encode(userPassword);
        user.setUserPassword(encodedPassword);
//        user.setUserPassword(userPassword);
        user.setUserStatus(UserStatus.ACTIVE);
        sequence++;
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
    }

    public User findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll(UserStatus userStatus) {
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
        if (userRepository.findByUserName(name) != null)
            return true;
        else return false;
    }

    public User findUserByName(String name) {
        return userRepository.findByUserName(name);
    }
}

