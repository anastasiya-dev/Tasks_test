package by.it.academy.service;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UtxoService {

    @Autowired
    @Value("#{blockchainUtxoDao}")
    BaseDao blockchainUtxoDao;

    @Autowired
    TransactionService transactionService;


    public Utxo createBcUtxo(String inputTransactionId) {
        Utxo utxo = new Utxo();
//        TransactionService transactionService = new TransactionService();
        System.out.println("tr id in create utxo: " + inputTransactionId);
        Transaction inputTr = transactionService.findTransactionById(inputTransactionId);
        utxo.setRecipient(inputTr.getRecipient());
        utxo.setValue(inputTr.getValue());
        utxo.setInputTransactionId(inputTransactionId);
        return utxo;
    }

    public ArrayList<Utxo> findAllUTXOs() {
        return (ArrayList<Utxo>) blockchainUtxoDao.findAll("");
    }

    public Utxo findUTXOById(String id) {
        return (Utxo) blockchainUtxoDao.findById(id);
    }

    public boolean createNewUTXO(Utxo utxo) {
        blockchainUtxoDao.create(utxo);
        return true;
    }
}
