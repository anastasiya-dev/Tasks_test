package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.repository.TransactionRepository;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public boolean saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        log.info("Saving transaction " + transaction);
        return true;
    }

    public Transaction findTransactionById(String id) {
        log.info("Extracting transaction from repository - by id: " + id);
        return transactionRepository.findById(id).get();
    }

    public ArrayList<Transaction> findAllTransactions() {
        log.info("Extracting all transactions");
        return (ArrayList<Transaction>) transactionRepository.findAll();
    }

    public boolean deleteTransaction(String id) {
        log.info("Deleting transaction from repository: " + id);
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
        transaction.setBlockId("n/a");
        log.info("Creating transaction: " + transaction);
        return transaction;
    }

    public void generateSignature(Transaction transaction, PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        transaction.setSignature(StringUtil.applyECDSASig(privateKey, data));
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.CONFIRMED);
        log.info("Generating signature for transaction: " + transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        log.info("Updating transaction");
        Transaction transactionSaved = transactionRepository.findById(transaction.getTransactionId()).get();
        log.info("Initial: " + transactionSaved);
        log.info("New: " + transaction);
        transactionSaved.setTransactionDateTime(transaction.getTransactionDateTime());
        transactionSaved.setSignature(transaction.getSignature());
        transactionSaved.setTransactionStatus(transaction.getTransactionStatus());
        transactionRepository.save(transactionSaved);
        return transactionRepository.findById(transactionSaved.getTransactionId()).get();
    }

    public Transaction walletDeleteTransaction(Transaction transaction) {
        Transaction transactionSaved = transactionRepository.findById(transaction.getTransactionId()).get();
        transactionSaved.setBlockId("wallet deletion");
        transactionRepository.save(transactionSaved);
        log.info("Creating wallet deletion transaction: " + transaction);
        return transactionRepository.findById(transactionSaved.getTransactionId()).get();
    }
}