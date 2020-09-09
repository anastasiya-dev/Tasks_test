package by.it.academy.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String userId;

    @Column(name = "user_name")
//    @Pattern(regexp = "[A-Z]+[a-z]+$",
//            message = "Username must be alphabetic with no spaces and first capital")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Transient
    private String confirmPassword;

    private String email;
    private String mobile;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Wallet> wallets = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
//                ", wallets=" + wallets.get(0).getWalletId() +
                '}';
    }
}
