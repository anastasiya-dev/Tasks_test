package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.repository.UtxoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

@Service
public class UtxoService {

    @Autowired
    UtxoRepository utxoRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    Utxo utxo;

    public Utxo createUtxo(String inputTransactionId) {
        Transaction inputTransaction = transactionService.findTransactionById(inputTransactionId);
        utxo.setValue(inputTransaction.getValue());
        utxo.setInputTransactionId(inputTransactionId);
        ArrayList<Utxo> utxos = (ArrayList<Utxo>) utxoRepository.findAll();
        if (utxos.size() == 0) {
            utxo.setUtxoId("0");
        } else {
            utxos.sort(Comparator.comparingInt(w -> Integer.parseInt(w.getUtxoId())));
            utxo.setUtxoId(String.valueOf(Integer.parseInt(utxos.get(utxos.size() - 1).getUtxoId()) + 1));
        }
        return utxo;
    }

    public ArrayList<Utxo> findAllUTXOs() {
        return (ArrayList<Utxo>) utxoRepository.findAll();
    }


    public Utxo findUTXOById(String id) {
        return utxoRepository.findById(id).get();
    }

    public boolean saveUtxo(Utxo utxo) {
        utxoRepository.save(utxo);
        return true;
    }

    public Utxo updateUtxo(Utxo utxoFromChain) {
        Utxo utxoSaved = utxoRepository.findById(utxoFromChain.getUtxoId()).get();
        utxoSaved.setWalletId(utxoFromChain.getWalletId());
        utxoSaved.setOutputTransactionId(utxoFromChain.getOutputTransactionId());
        utxoRepository.save(utxoSaved);
        return utxoRepository.findById(utxoSaved.getUtxoId()).get();
    }
}