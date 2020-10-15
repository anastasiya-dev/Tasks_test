package sensors.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sensors.pojo.User;
import sensors.repository.UserRepository;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findUserByName(String name) {
        try {
            log.info("Extracting user from repository - by name: " + name);
            User user = userRepository.findByUserName(name);
            if (user == null) {
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
                    .filter(u -> u.getUserName().equals(name))
                    .findFirst()
                    .orElseThrow();
        }
    }
}
