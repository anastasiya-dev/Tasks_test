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
public class BlockTemporary {
    @Id
    private String blockTemporaryId;
    private String blockId;

    private String hash;
    private String previousHash;
    private String merkleRoot;

    private long timeStamp;
    private int nonce;

    @Override
    public String toString() {
        return "BlockTemporary{" +
                "blockTemporaryId='" + blockTemporaryId + '\'' +
                ", blockId='" + blockId + '\'' +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                '}';
    }
}
