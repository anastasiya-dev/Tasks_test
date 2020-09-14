//package by.it.academy.pojo;
//
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//
//@Entity
//@Getter
//@Setter
//public class TransactionInput {
//
//    @Id
//    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
//
//    @OneToOne
//    public TransactionOutput transactionOutput; //Contains the Unspent transaction output
//
//    @ManyToOne
//    public Transaction transaction;
//
//    @Override
//    public String toString() {
//        return "TransactionInput{" +
//                "transactionOutputId='" + transactionOutputId +
//                '}';
//    }
//}
//
