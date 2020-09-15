package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Wallet {

    @Id
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name="wallet_id")
    private String walletId;

    @Column(name = "private_key")
    public PrivateKey privateKey;
    @Column(name = "private_key_string")
    public String privateKeyString;
    @Column(name = "public_key")
    public PublicKey publicKey;
//    @Column(name = "public_key_string")
//    public String publicKeyString;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Utxo> UTXOs = new ArrayList<>(); //only UTXOs owned by this wallet.

    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId='" + walletId + '\'' +
                ", privateKeyString='" + privateKeyString + '\'' +
//                ", publicKeyString='" + publicKeyString + '\'' +
                ", UTXOs=" + UTXOs +
//                ", user=" + user +
                '}';
    }
}
