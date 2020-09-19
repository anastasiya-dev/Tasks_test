package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.repository.TransactionRepository;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    WalletService walletService;
    @Autowired
    Transaction transaction;

    public boolean saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        return true;
    }

    public Transaction findTransactionById(String id) {
        return (Transaction) transactionRepository.findById(id).get();
    }

    public ArrayList<Transaction> findAllTransactions() {
        return (ArrayList<Transaction>) transactionRepository.findAll();
    }

    public boolean deleteTransaction(String id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Transaction createTransaction(String from, String to, float value) {
        transaction.setTransactionId(String.valueOf(new Date().getTime()));
        transaction.setSender((walletService.findWalletById(from)).getPublicKey());
        transaction.setSenderId(from);
        transaction.setRecipient((walletService.findWalletById(to)).getPublicKey());
        transaction.setRecipientId(to);
        transaction.setValue((float) Math.round(value * 10.0) / 10.0f);
        transaction.setTransactionStatus(TransactionStatus.CREATED);
        return transaction;
    }

    public void generateSignature(Transaction transaction, PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        transaction.setSignature(StringUtil.applyECDSASig(privateKey, data));
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.CONFIRMED);
    }

    public Transaction updateTransaction(Transaction transaction) {
        Transaction transactionSaved = transactionRepository.findById(transaction.getTransactionId()).get();
        transactionSaved.setTransactionDateTime(transaction.getTransactionDateTime());
        transactionSaved.setSignature(transaction.getSignature());
        transactionSaved.setTransactionStatus(transaction.getTransactionStatus());
        transactionRepository.save(transactionSaved);
        return transactionRepository.findById(transactionSaved.getTransactionId()).get();
    }
}