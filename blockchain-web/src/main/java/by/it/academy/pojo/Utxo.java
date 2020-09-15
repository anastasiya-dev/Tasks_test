package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.security.PublicKey;

@Entity
@Setter
@Getter
public class Utxo {

    @Id
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "utxo_id")
    public String utxoId;
    public PublicKey recipient;
    public float value;
    @Column(name = "input_transaction_id")
    public String inputTransactionId;
    @Column(name = "output_transaction_id")
    public String outputTransactionId;

//    @ManyToOne
    @Column(name = "wallet_id")
    public String walletId;

    @Override
    public String toString() {
        return "Utxo{" +
                "UtxoId='" + utxoId + '\'' +
                ", recipient=" + recipient +
                ", value=" + value +
                ", inputTransactionId='" + inputTransactionId + '\'' +
                ", outputTransactionId='" + outputTransactionId + '\'' +
//                ", wallet=" + wallet +
                '}';
    }
}
