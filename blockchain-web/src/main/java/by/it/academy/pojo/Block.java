package by.it.academy.pojo;

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
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "block_id")
    public String blockId;

    public String hash;
    @Column(name = "previous_hash")
    public String previousHash;
    @Column(name = "merkle_root")
    public String merkleRoot;
    @Column(name = "time_stamp")
    private long timeStamp;
    private int nonce;

//    @OneToMany(mappedBy = "block", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    public List<Transaction> transactions = new ArrayList<>();

    @Override
    public String toString() {
        return "Block{" +
                "blockId='" + blockId + '\'' +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
//                ", transactions=" + transactions +
                '}';
    }
}
