package by.it.academy.service;

import by.it.academy.pojo.Utxo;
import by.it.academy.repository.UtxoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UtxoService {

    @Autowired
    UtxoRepository utxoRepository;
    @Autowired
    Utxo utxo;

    private static final Logger log = LoggerFactory.getLogger(UtxoService.class);

    public Utxo createUtxo(String inputTransactionId, float value, String walletId) {
        utxo.setValue((float) Math.round(value * 10.0) / 10.0f);
        utxo.setInputTransactionId(inputTransactionId);
        utxo.setUtxoId(String.valueOf(new Date().getTime()));
        utxo.setWalletId(walletId);
        Utxo saved = utxoRepository.save(utxo);
        log.info("Saving utxo: " + saved);
        return saved;
    }

    public ArrayList<Utxo> findAllUTXOs() {
        log.info("Extracting all utxo-s");
        return (ArrayList<Utxo>) utxoRepository.findAll();
    }


    public Utxo findUTXOById(String id) {
        log.info("Extracting utxo from repository - by id: " + id);
        return utxoRepository.findById(id).get();
    }


    public Utxo updateUtxo(Utxo utxoFromChain) {
        log.info("Updating utxo");
        Utxo utxoSaved = utxoRepository.findById(utxoFromChain.getUtxoId()).get();
        log.info("Initial: " + utxoSaved);
        log.info("New: " + utxoFromChain);
        utxoSaved.setWalletId(utxoFromChain.getWalletId());
        utxoSaved.setOutputTransactionId(utxoFromChain.getOutputTransactionId());
        utxoRepository.save(utxoSaved);
        return utxoRepository.findById(utxoSaved.getUtxoId()).get();
    }
}