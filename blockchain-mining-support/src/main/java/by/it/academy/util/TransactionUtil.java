package by.it.academy.util;

import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BlockchainUtxoDao;
import by.it.academy.service.TransactionService;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;

//
//@Service
public class TransactionUtil {


    //    @Autowired
    TransactionService transactionService;

    //    @Autowired
    BlockchainUtxoDao blockchainUtxoDao;

    // Constructor:
    public static Transaction createTransaction(PublicKey from, PublicKey to, float value) {
        Transaction transaction = new Transaction();
        transaction.setSender(from);
        transaction.setSenderString(StringUtil.getStringFromKey(from));
        transaction.setRecipient(to);
        transaction.setRecipientString(StringUtil.getStringFromKey(to));
        transaction.setValue(value);
//        transaction.setInputs(inputs);
        return transaction;
    }

    // This Calculates the transaction hash (which will be used as its Id)
    private static String calulateHash(Transaction transaction) {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(transaction.getSender()) +
                        StringUtil.getStringFromKey(transaction.getRecipient()) +
                        Float.toString(transaction.getValue()) + transaction.getTransactionId()
        );
    }

    //Signs all the data we don't wish to be tampered with.
    public static void generateSignature(Transaction transaction, PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        transaction.setSignature(StringUtil.applyECDSASig(privateKey, data));
        transaction.setTransactionDateTime(LocalDateTime.now());
    }

}
