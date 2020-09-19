package by.it.academy.pojo;

import by.it.academy.support.TransactionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Getter
@Setter
@Component
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;
    private PublicKey sender; // senders address/public key.
    @Column(name = "sender_id")
    private String senderId;
    public PublicKey recipient; // Recipients address/public key.
    @Column(name = "recipient_id")
    private String recipientId;
    private float value;
    private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
    @Column(name = "transaction_date_time")
    private LocalDateTime transactionDateTime;

    @Column(name = "block_id")
    private String blockId;

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", value=" + value +
                ", signature=" + Arrays.toString(signature) +
                ", transactionDateTime=" + transactionDateTime +
                ", blockId='" + blockId + '\'' +
                '}';
    }
}