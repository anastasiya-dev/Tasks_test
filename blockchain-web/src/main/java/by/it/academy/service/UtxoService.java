package by.it.academy.service;

import by.it.academy.pojo.Utxo;
import by.it.academy.repository.UtxoRepository;
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

    public Utxo createUtxo(String inputTransactionId, float value, String walletId) {
        utxo.setValue(value);
        utxo.setInputTransactionId(inputTransactionId);
        utxo.setUtxoId(String.valueOf(new Date().getTime()));
        utxo.setWalletId(walletId);
        Utxo saved = utxoRepository.save(utxo);
        return saved;
    }

    public ArrayList<Utxo> findAllUTXOs() {
        return (ArrayList<Utxo>) utxoRepository.findAll();
    }


    public Utxo findUTXOById(String id) {
        return utxoRepository.findById(id).get();
    }


    public Utxo updateUtxo(Utxo utxoFromChain) {
        Utxo utxoSaved = utxoRepository.findById(utxoFromChain.getUtxoId()).get();
        utxoSaved.setWalletId(utxoFromChain.getWalletId());
        utxoSaved.setOutputTransactionId(utxoFromChain.getOutputTransactionId());
        utxoRepository.save(utxoSaved);
        return utxoRepository.findById(utxoSaved.getUtxoId()).get();
    }
}