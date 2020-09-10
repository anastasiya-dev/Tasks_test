package by.it.academy.util;

import by.it.academy.pojo.TransactionInput;

public class TransactionInputUtil {
    public static TransactionInput createTransactionInput(String transactionOutputId) {
        TransactionInput transactionInput = new TransactionInput();
        transactionInput.setTransactionOutputId(transactionOutputId);
        return transactionInput;
    }
}
