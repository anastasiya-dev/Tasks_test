package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.PublicKey;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    public String transactionId; // this is also the hash of the transaction.
    public PublicKey sender; // senders address/public key.
    public PublicKey reciepient; // Recipients address/public key.
    public float value;
    public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

}
