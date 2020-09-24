package by.it.academy.pojo;

import by.it.academy.support.WalletStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.security.PrivateKey;
import java.security.PublicKey;

@Entity
@Getter
@Setter
@Component
@Scope(value = "prototype")
public class Wallet {

    @Id
    @Column(name = "wallet_id")
    private String walletId;
    @Column(name = "wallet_status")
    private WalletStatus walletStatus;
    @Column(name = "private_key")
    private PrivateKey privateKey;
    @Column(name = "private_key_string")
    private String privateKeyString;
    @Column(name = "public_key")
    private PublicKey publicKey;
    @Column(name = "public_key_string")
    private String publicKeyString;

    @Transient
    private float balance;
    @Column(name = "user_id")
    private String userId;

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId='" + walletId + '\'' +
                ", privateKeyString='" + privateKeyString + '\'' +
                ", publicKeyString='" + publicKeyString + '\'' +
                '}';
    }
}
