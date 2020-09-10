package by.it.academy.util;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionOutput;

import java.security.PublicKey;

public class TransactionOutputUtil {
    //Constructor
    public static TransactionOutput createTransactionOutput(PublicKey reciepient, float value, Transaction transaction) {
        TransactionOutput transactionOutput = new TransactionOutput();
        transactionOutput.setReciepient(reciepient);
        transactionOutput.setValue(value);
        transactionOutput.setTransaction(transaction);
        transactionOutput.setId(StringUtil.applySha256(StringUtil.getStringFromKey(reciepient) + Float.toString(value) + transaction.getTransactionId()));
        return transactionOutput;
    }

    //Check if coin belongs to you
    public static boolean isMine(TransactionOutput transactionOutput, PublicKey publicKey) {
        return (publicKey == transactionOutput.getReciepient());
    }
}