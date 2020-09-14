package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.security.PublicKey;

@Entity
@Setter
@Getter
public class Utxo {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")

    public String blockchainUtxoId;
    public PublicKey recipient;
    public float value;
    public String inputTransactionId;
    public String outputTransactionId;

    @ManyToOne
    public Wallet wallet;

    @Override
    public String toString() {
        return "BlockchainUtxo{" +
                "blockchainUtxoId='" + blockchainUtxoId + '\'' +
                ", recipient=" + recipient +
                ", value=" + value +
                ", inputTransactionId='" + inputTransactionId + '\'' +
                ", outputTransactionId='" + outputTransactionId + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
