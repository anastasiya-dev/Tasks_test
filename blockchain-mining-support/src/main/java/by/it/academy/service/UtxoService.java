package by.it.academy.service;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.UtxoDao;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

//@Service
public class UtxoService {

    public Utxo createBcUtxo(SessionFactory sessionFactory, String inputTransactionId) {
        Utxo utxo = new Utxo();
        TransactionService transactionService = new TransactionService();
        Transaction inputTr = transactionService.findTransactionById(sessionFactory, inputTransactionId);
        utxo.setRecipient(inputTr.getRecipient());
        utxo.setValue(inputTr.getValue());
        utxo.setInputTransactionId(inputTransactionId);
        return utxo;
    }

    UtxoDao utxoDao = new UtxoDao();


    public ArrayList<Utxo> findAllUTXOs(SessionFactory sessionFactory) {
        return (ArrayList<Utxo>) utxoDao.findAll(sessionFactory, "");
    }

    public Utxo findUTXOById(SessionFactory sessionFactory, String id) {
        return (Utxo) utxoDao.findById(sessionFactory, id);
    }

    public boolean createNewUTXO(SessionFactory sessionFactory, Utxo utxo) {
        utxoDao.create(sessionFactory, utxo);
        return true;
    }
}
