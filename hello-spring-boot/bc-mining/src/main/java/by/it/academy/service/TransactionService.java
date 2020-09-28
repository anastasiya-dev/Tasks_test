package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.TransactionRepository;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import java.io.IOException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

@Service
//@Transactional
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    Transaction transaction;
    @Autowired
    WalletService walletService;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(TransactionService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public boolean saveTransaction(Transaction transaction) {
        logger.info("Saving transaction: " + transaction);
        transactionRepository.save(transaction);
        return true;
    }

    public Transaction findTransactionById(String id) {
        logger.info("Extracting transaction from repository - by id: " + id);
        return transactionRepository.findById(id).orElse(null);
    }

    public ArrayList<Transaction> findAllTransactions() {
        logger.info("Extracting all transactions");
        return (ArrayList<Transaction>) transactionRepository.findAll();
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Transaction updateTransaction(Transaction transaction) {
        logger.info("Updating transaction");
        String id = transaction.getTransactionId();
        Transaction savedTransaction = transactionRepository.findById(id).orElseThrow();
        logger.info("Initial: " + savedTransaction);
        logger.info("New: " + transaction);
        if (savedTransaction.equals(transaction)) {
            return savedTransaction;
        } else {
            savedTransaction.setSignature(transaction.getSignature());
            savedTransaction.setTransactionDateTime(transaction.getTransactionDateTime());
            savedTransaction.setTransactionStatus(transaction.getTransactionStatus());
            transactionRepository.save(savedTransaction);
        }
        return transactionRepository.findById(id).orElseThrow();
    }

    // Constructor:
    public Transaction createTransaction(String from, String to, float value) {
        transaction.setTransactionId(String.valueOf(new Date().getTime()));
        transaction.setSender((walletService.findWalletById(from)).getPublicKey());
        transaction.setSenderId(from);
        transaction.setRecipient((walletService.findWalletById(to)).getPublicKey());
        transaction.setRecipientId(to);
        transaction.setValue((float) Math.round(value * 10.0) / 10.0f);
        transaction.setTransactionStatus(TransactionStatus.CREATED);
        transaction.setBlockId("not mined");
        logger.info("Creating transaction: " + transaction);
        return transaction;
    }


    //Signs all the data we don't wish to be tampered with.
    public void generateSignature(Transaction transaction, PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        transaction.setSignature(StringUtil.applyECDSASig(privateKey, data));
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.CONFIRMED);
        logger.info("Generating signature for transaction: " + transaction);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Transaction updateTransaction(Block block, Transaction transaction) {
        logger.info("Updating transaction");
        String id = transaction.getTransactionId();
        Transaction savedTransaction = transactionRepository.findById(id).orElseThrow();
        logger.info("Initial: " + savedTransaction);
        logger.info("New: " + transaction);
        if (savedTransaction.equals(transaction)) {
            return savedTransaction;
        } else {
            savedTransaction.setBlockId(block.getBlockId());
            transactionRepository.save(savedTransaction);
        }
        return transactionRepository.findById(id).orElseThrow();
    }
}
