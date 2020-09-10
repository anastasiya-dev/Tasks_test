package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
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
