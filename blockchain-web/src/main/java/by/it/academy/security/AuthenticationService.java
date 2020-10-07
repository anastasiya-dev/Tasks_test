package by.it.academy.security;

import by.it.academy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("authService")
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserService userService;

    private static Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    public static String userId = null;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Calling loadUserByUserName: {}", username);
        by.it.academy.pojo.User appUser = userService.findUserByName(username);
        log.info("found appUser:" + appUser);
        if (appUser == null) throw new UsernameNotFoundException("User not found: " + username);
        userId = appUser.getUserId();
        log.info("set user ID:" + userId);
        return new User(
                appUser.getUserName(),
                appUser.getUserPassword(),
                appUser.getRoles()
                        .stream()
                        .map(appRole -> new SimpleGrantedAuthority("ROLE_" + appRole.getRoleName()))
                        .collect(Collectors.toList()));
    }
}
