package by.it.academy.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Component
public class UserApprole {
    @Id
    private String userUserId;
    private String rolesId;

}
