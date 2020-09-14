package by.it.academy.service;

import by.it.academy.Start;
import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BlockchainUtxoDao;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

//@Service
public class BlockchainUtxoService {

    public BlockchainUtxo createBcUtxo(SessionFactory sessionFactory, String inputTransactionId) {
        BlockchainUtxo blockchainUtxo = new BlockchainUtxo();
        TransactionService transactionService = new TransactionService();
        Transaction inputTr = transactionService.findTransactionById(sessionFactory, inputTransactionId);
        blockchainUtxo.setRecipient(inputTr.getRecipient());
        blockchainUtxo.setValue(inputTr.getValue());
        blockchainUtxo.setInputTransactionId(inputTransactionId);
        return blockchainUtxo;
    }

    //    @Autowired
//    @Value("#{blockchainUtxoDao}")
    BlockchainUtxoDao blockchainUtxoDao = new BlockchainUtxoDao();


    public ArrayList<BlockchainUtxo> findAllUTXOs(SessionFactory sessionFactory) {
        return (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll(sessionFactory, "");
    }

    public BlockchainUtxo findUTXOById(SessionFactory sessionFactory, String id) {
        return (BlockchainUtxo) blockchainUtxoDao.findById(sessionFactory, id);
    }

    public boolean createNewUTXO(SessionFactory sessionFactory, BlockchainUtxo blockchainUtxo) {
        blockchainUtxoDao.create(sessionFactory, blockchainUtxo);
        return true;
    }
}
