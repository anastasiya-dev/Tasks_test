package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.repository.UtxoRepository;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class UtxoService {

    @Autowired
    UtxoRepository utxoRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    Utxo utxo;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(UtxoService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Utxo createUtxo(String inputTransactionId) {
        Transaction inputTransaction = transactionService.findTransactionById(inputTransactionId);
        utxo.setValue((float) Math.round(inputTransaction.getValue() * 10.0) / 10.0f);
        utxo.setInputTransactionId(inputTransactionId);
        utxo.setUtxoId(String.valueOf(new Date().getTime()));
        logger.info("Creating utxo: " + utxo);
        return utxo;
    }

    public ArrayList<Utxo> findAllUTXOs() {
        logger.info("Extracting all utxo-s");
        return (ArrayList<Utxo>) utxoRepository.findAll();
    }

    public Utxo findUTXOById(String id) {
        logger.info("Extracting utxo from repository - by id: " + id);
        return utxoRepository.findById(id).orElse(null);
    }

    public boolean saveUtxo(Utxo utxo) {
        logger.info("Saving utxo: " + utxo);
        utxoRepository.save(utxo);
        return true;
    }
}
