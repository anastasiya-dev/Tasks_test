package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

@Entity
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String walletId;

    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>(); //only UTXOs owned by this wallet.

}
