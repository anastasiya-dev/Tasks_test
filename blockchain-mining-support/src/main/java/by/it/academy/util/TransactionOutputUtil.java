package by.it.academy.util;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionOutput;

import java.security.PublicKey;

public class TransactionOutputUtil {
    //Constructor
    public static TransactionOutput createTransactionOutput(PublicKey recipient, float value, Transaction transaction) {
        TransactionOutput transactionOutput = new TransactionOutput();
        transactionOutput.setRecipient(recipient);
        transactionOutput.setValue(value);
        transactionOutput.setTransaction(transaction);
        transactionOutput.setId(StringUtil.applySha256(StringUtil.getStringFromKey(recipient) + Float.toString(value) + transaction.getTransactionId()));
        return transactionOutput;
    }
}
