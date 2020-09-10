package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class TransactionInput {

    @Id
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId

//    public List<TransactionOutput> UTXO = new ArrayList<TransactionOutput>(); //Contains the Unspent transaction output

    @OneToOne
    public TransactionOutput transactionOutput; //Contains the Unspent transaction output
}

