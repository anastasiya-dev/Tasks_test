package by.it.academy.util;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionInput;
import by.it.academy.repository.TransactionOutputDao;

public class TransactionInputUtil {

    public static TransactionInput createTransactionInput(Transaction transaction, String transactionOutputId) {
        TransactionOutputDao transactionOutputDao = new TransactionOutputDao();
        TransactionInput transactionInput = new TransactionInput();
        transactionInput.setTransactionOutputId(transactionOutputId);
        transactionInput.setTransaction(transaction);
        return transactionInput;
    }
}
