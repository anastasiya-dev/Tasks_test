package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Arrays;

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

    @ManyToOne
    private Block block;

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", senderString='" + senderString + '\'' +
                ", recipientString='" + recipientString + '\'' +
                ", value=" + value +
                ", signature=" + Arrays.toString(signature) +
                ", transactionDateTime=" + transactionDateTime +
//                ", block=" + block +
                '}';
    }
}
