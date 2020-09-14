package by.it.academy.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Block {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    public String blockId;

    public String hash;
    public String previousHash;
    public String merkleRoot;

    private long timeStamp;
    private int nonce;

    @OneToMany(mappedBy = "block", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Transaction> transactions = new ArrayList<>();

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
