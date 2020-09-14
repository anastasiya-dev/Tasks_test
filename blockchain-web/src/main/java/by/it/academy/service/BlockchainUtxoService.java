package by.it.academy.service;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BlockchainUtxoService {

    @Autowired
    @Value("#{blockchainUtxoDao}")
    BaseDao blockchainUtxoDao;

    @Autowired
    TransactionService transactionService;


    public BlockchainUtxo createBcUtxo(String inputTransactionId) {
        BlockchainUtxo blockchainUtxo = new BlockchainUtxo();
//        TransactionService transactionService = new TransactionService();
        System.out.println("tr id in create utxo: " + inputTransactionId);
        Transaction inputTr = transactionService.findTransactionById(inputTransactionId);
        blockchainUtxo.setRecipient(inputTr.getRecipient());
        blockchainUtxo.setValue(inputTr.getValue());
        blockchainUtxo.setInputTransactionId(inputTransactionId);
        return blockchainUtxo;
    }

    public ArrayList<BlockchainUtxo> findAllUTXOs() {
        return (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll("");
    }

    public BlockchainUtxo findUTXOById(String id) {
        return (BlockchainUtxo) blockchainUtxoDao.findById(id);
    }

    public boolean createNewUTXO(BlockchainUtxo blockchainUtxo) {
        blockchainUtxoDao.create(blockchainUtxo);
        return true;
    }
}
