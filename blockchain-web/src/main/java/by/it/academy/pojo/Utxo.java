package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@Component
public class Utxo {

    @Id
    @Column(name = "utxo_id")
    private String utxoId;
    private float value;
    @Column(name = "input_transaction_id")
    private String inputTransactionId;
    @Column(name = "output_transaction_id")
    private String outputTransactionId;
    @Column(name = "wallet_id")
    private String walletId;

    @Override
    public String toString() {
        return "Utxo{" +
                "utxoId='" + utxoId + '\'' +
                ", value=" + value +
                ", inputTransactionId='" + inputTransactionId + '\'' +
                ", outputTransactionId='" + outputTransactionId + '\'' +
                ", walletId='" + walletId + '\'' +
                '}';
    }
}
