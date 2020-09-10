package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionOutputService {

    @Autowired
    @Value("#{transactionOutputDao}")
    BaseDao transactionOutputDao;

    public ArrayList<Transaction> findAllTransactions() {
        return (ArrayList<Transaction>) transactionOutputDao.findAll("");
    }
}
