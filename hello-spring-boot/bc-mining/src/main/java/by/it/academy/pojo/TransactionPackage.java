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
public class TransactionPackage {

    @Id
    private String transactionPackageId;
    private String blockAttemptedId;
    private String transactionId;
    private String miningSessionId;

    @Override
    public String toString() {
        return "TransactionPackage{" +
                "transactionPackageId='" + transactionPackageId + '\'' +
                ", blockAttemptedId='" + blockAttemptedId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", miningSessionId='" + miningSessionId + '\'' +
                '}';
    }
}
