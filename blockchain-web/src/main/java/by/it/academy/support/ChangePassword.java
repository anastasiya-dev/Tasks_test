package by.it.academy.support;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ChangePassword {
    String oldPassword;
    String newPassword;
    String confirmPassword;
}
