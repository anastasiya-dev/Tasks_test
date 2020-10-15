package sensors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sensors.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserName(String userName);
}
