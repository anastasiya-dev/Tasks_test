package by.it.academy.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    public String transactionId; // this is also the hash of the transaction.
    public PublicKey sender; // senders address/public key.
    public String senderString;
    public PublicKey recipient; // Recipients address/public key.
    public String recipientString;
    public float value;
    public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
    public LocalDateTime transactionDateTime;

//    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    public List<TransactionInput> inputs = new ArrayList<TransactionInput>();
//
//    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Fetch(FetchMode.SELECT)
//    public List<TransactionOutput> outputs = new ArrayList<>();

    @ManyToOne
    private Block block;

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
//                ", sender=" + sender +
                ", senderString='" + senderString + '\'' +
//                ", recipient=" + recipient +
                ", recipientString='" + recipientString + '\'' +
                ", value=" + value +
                ", signature=" + Arrays.toString(signature) +
                ", transactionDateTime=" + transactionDateTime +
//                ", inputs=" + inputs +
//                ", outputs=" + outputs +
//                ", blocks=" + blocks +
                '}';
    }
}
