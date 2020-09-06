package by.it.academy.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.PublicKey;

@Entity
@Getter
@Setter
public class TransactionOutput {
    @Id
    public String id;
    public PublicKey reciepient; //also known as the new owner of these coins.
    public float value; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in
}
