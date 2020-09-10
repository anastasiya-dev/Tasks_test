package by.it.academy.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    public String transactionId; // this is also the hash of the transaction.
    public PublicKey sender; // senders address/public key.
    public String senderString;
    public PublicKey recipient; // Recipients address/public key.
    public String recipientString;
    public float value;
    public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
    public LocalDateTime transactionDateTime;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
//    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<TransactionOutput> outputs = new ArrayList<>();

    @ManyToMany(mappedBy = "transactions")
    @EqualsAndHashCode.Exclude
    private Set<Block> blocks = new HashSet<>();

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", value=" + value +
                ", date time =" + transactionDateTime +
                ", signature=" + Arrays.toString(signature) +
//                ", inputs=" + inputs +
                ", outputs=" + outputs +
                '}';
    }
}
