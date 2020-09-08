package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionService {

    @Autowired
    @Value("#{transactionDao}")
    BaseDao transactionDao;

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
}
