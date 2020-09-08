package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionService {

    @Autowired
    @Value("#{transactionDao}")
    BaseDao transactionDao;

    @Autowired
    WalletService walletService;

    public boolean createNewTransaction(Transaction transaction) {
        transactionDao.create(transaction);
        return true;
    }

    public Transaction findTransactionById(String id) {
        return (Transaction) transactionDao.findById(id);
    }

    public ArrayList<Transaction> findAllTransactions() {
        return (ArrayList<Transaction>) transactionDao.findAll("");
    }

    public boolean deleteTransaction(Transaction transaction) {
        return transactionDao.delete(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return (Transaction) transactionDao.update(transaction);
    }


    public ArrayList<Transaction> getAllForWallet(String walletId) {
        ArrayList<Transaction> transactionsAll = (ArrayList<Transaction>) transactionDao.findAll("");
        Wallet wallet = walletService.findWalletById(walletId);
        String publicKey = StringUtil.getStringFromKey(wallet.getPublicKey());
        ArrayList<Transaction> transactionsForWallet = new ArrayList<>();
        for (Transaction transaction : transactionsAll) {
            if (StringUtil.getStringFromKey(transaction.getReciepient())
                    .equals(StringUtil.getStringFromKey(transaction.getSender()))) {
                transactionsForWallet.add(transaction);
                transaction.setValue(transaction.getValue() * Float.valueOf(-1));
                transactionsForWallet.add(transaction);
            } else if (StringUtil.getStringFromKey(transaction.getSender()).equals(publicKey)) {
                transaction.setValue(transaction.getValue() * Float.valueOf(-1));
                transactionsForWallet.add(transaction);
            } else if (StringUtil.getStringFromKey(transaction.getReciepient()).equals(publicKey)) {
                transactionsForWallet.add(transaction);
            }
        }
        return transactionsForWallet;
    }
}
