package by.it.academy.service;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.repository.BlockchainUtxoDao;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

//@Service
public class BlockchainUtxoService {

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
