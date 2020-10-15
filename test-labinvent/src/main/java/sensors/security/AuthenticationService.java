package sensors.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sensors.service.UserService;

import java.util.stream.Collectors;

@Slf4j
@Service("authService")
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        sensors.pojo.User appUser = userService.findUserByName(username);
        if (appUser == null) throw new UsernameNotFoundException("User not found: " + username);
        return new User(
                appUser.getUserName(),
                appUser.getUserPassword(),
                appUser.getRoles()
                        .stream()
                        .map(appRole -> new SimpleGrantedAuthority("ROLE_" + appRole.getRoleName()))
                        .collect(Collectors.toList()));
    }
}
