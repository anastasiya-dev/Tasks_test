package by.it.academy.repository;

import by.it.academy.pojo.User;
import by.it.academy.support.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserName(String userName);

    User findByUserNameAndUserStatus(String name, UserStatus userStatus);
}
