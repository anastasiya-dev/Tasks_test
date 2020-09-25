package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Component
@Getter
@Setter
public class Block {
    @Id
    private String blockId;

    private String hash;
    private String previousHash;
    private String merkleRoot;

    private long timeStamp;
    private int nonce;

    private String miningSessionId;
    private String minerId;

    @Override
    public String toString() {
        return "Block{" +
                "blockId='" + blockId + '\'' +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                ", miningSessionId='" + miningSessionId + '\'' +
                ", minerId='" + minerId + '\'' +
                '}';
    }
}
