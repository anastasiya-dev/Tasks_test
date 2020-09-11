package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.security.PublicKey;

@Entity
@Setter
@Getter
public class BlockchainUtxo {

    @Id
    //transactionOutputId
            String blockchainUtxoId;
    public PublicKey recipient;
    public float value;
    public String parentTransactionId;
    public String transactionInputId;

    @ManyToOne
    public Wallet wallet;

    @Override
    public String toString() {
        return "BlockchainUtxo{" +
                "blockchainUtxoId='" + blockchainUtxoId + '\'' +
                ", recipient=" + recipient +
                ", value=" + value +
                ", parentTransactionId='" + parentTransactionId + '\'' +
                ", transactionInputId='" + transactionInputId + '\'' +
                '}';
    }
}
