package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class TransactionInput {

    @Id
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId

    public ArrayList<TransactionOutput> UTXO = new ArrayList<TransactionOutput>(); //Contains the Unspent transaction output
}

