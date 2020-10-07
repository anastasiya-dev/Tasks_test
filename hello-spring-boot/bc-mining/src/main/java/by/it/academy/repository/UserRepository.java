package by.it.academy.repository;

import by.it.academy.pojo.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findByUserName(String name);
}
