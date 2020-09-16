package by.it.academy.service;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BaseDao;
import by.it.academy.support.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

@Service
public class UtxoService {

    @Autowired
    @Value("#{utxoDao}")
    BaseDao utxoDao;

    @Autowired
    TransactionService transactionService;

    public Utxo createBcUtxo(String inputTransactionId) {
        Utxo utxo = new Utxo();
        Transaction inputTr = transactionService.findTransactionById(inputTransactionId);
        utxo.setRecipient(inputTr.getRecipient());
        utxo.setValue(inputTr.getValue());
        utxo.setInputTransactionId(inputTransactionId);
        ArrayList<Utxo> utxos = (ArrayList<Utxo>) utxoDao.findAll("");
        if (utxos.size() == 0) {
            utxo.setUtxoId("0");
        } else {
            utxos.sort(Comparator.comparingInt(w -> Integer.valueOf(w.getUtxoId())));
            utxo.setUtxoId(String.valueOf(Integer.valueOf(utxos.get(utxos.size() - 1).getUtxoId()) + 1));
        }
        return utxo;
    }

    public ArrayList<Utxo> findAllUTXOs() {

        return (ArrayList<Utxo>) utxoDao.findAll("");
    }



    public Utxo findUTXOById(String id) {
        return (Utxo) utxoDao.findById(id);
    }

    public boolean createNewUTXO(Utxo utxo) {
        utxoDao.create(utxo);
        return true;
    }
}