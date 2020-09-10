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
