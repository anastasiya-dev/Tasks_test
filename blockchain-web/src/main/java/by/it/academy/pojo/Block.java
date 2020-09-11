package by.it.academy.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Block {
    @Id
    public String hash;
    public String previousHash;
    public String merkleRoot;

    private long timeStamp;
    private int nonce;

    @ManyToMany
    @JoinTable(
            name = "block_transaction",
            joinColumns = {
                    @JoinColumn(name = "block_hash")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "transaction_transactionId")
            }
    )
    @EqualsAndHashCode.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                ", transactions=" + transactions +
                '}';
    }
}
