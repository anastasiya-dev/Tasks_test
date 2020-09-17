package by.it.academy.util;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import by.it.academy.repository.UtxoDao;
import by.it.academy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;

//
//@Service
public class TransactionUtil {


    @Autowired
    TransactionService transactionService;

    @Autowired
    UtxoDao utxoDao;


    //!!!not used
    // This Calculates the transaction hash (which will be used as its Id)
    private static String calulateHash(Transaction transaction) {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(transaction.getSender()) +
                        StringUtil.getStringFromKey(transaction.getRecipient()) +
                        Float.toString(transaction.getValue()) + transaction.getTransactionId()
        );
    }



}