package by.it.academy.service;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BlockchainUtxoDao;
import by.it.academy.repository.WalletDao;
import by.it.academy.util.BlockchainUtxoUtil;
import by.it.academy.util.TransactionUtil;
import org.hibernate.SessionFactory;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

//@Service
public class WalletService {

    //    @Autowired
//    @Value("#{walletDao}")
    WalletDao walletDao = new WalletDao();

    //    @Autowired
//    @Value("#{blockchainUtxoDao}")
    BlockchainUtxoDao blockchainUtxoDao = new BlockchainUtxoDao();

    BlockchainUtxoService blockchainUtxoService = new BlockchainUtxoService();

    //    @Autowired
//    TransactionService transactionService = new TransactionService();

    public boolean createNewWallet(SessionFactory factory, Wallet wallet) {
        walletDao.create(factory, wallet);
        return true;
    }

    public List<Wallet> getAll(SessionFactory factory, String userId) {
        List<Wallet> all = walletDao.findAll(factory, "");
        List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : all) {
            if (wallet.getUser().getUserId().equals(userId)) {
                wallets.add(wallet);
            }
        }
        return wallets;
    }

    public Wallet findWalletById(SessionFactory sessionFactory, String id) {
        return (Wallet) walletDao.findById(sessionFactory, id);
    }

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance(SessionFactory sessionFactory, Wallet wallet) {
        float total = 0;
        ArrayList<BlockchainUtxo> UTXOs = (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll(sessionFactory, "");
        for (BlockchainUtxo UTXO : UTXOs) {
            if (BlockchainUtxoUtil.isMine(UTXO, wallet)) { //if output belongs to me ( if coins belong to me )
//                wallet.UTXOs.add(UTXO); //add it to our list of unspent transactions.
//                walletDao.create(wallet);
                System.out.println(UTXO.blockchainUtxoId + " + " + UTXO.getValue());
                total += UTXO.value;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(SessionFactory sessionFactory, Wallet wallet, PublicKey recipient, float value, Transaction transaction) {

        float total = 0;
        for (BlockchainUtxo UTXO : wallet.UTXOs) {
            total += UTXO.value;
            UTXO.setOutputTransactionId(transaction.transactionId);
            wallet.UTXOs.remove(UTXO);
            if (total > value) break;
        }

        createNewWallet(sessionFactory, wallet);
        TransactionUtil.generateSignature(transaction, wallet.privateKey);
        BlockchainUtxo blockchainUtxo = blockchainUtxoService.createBcUtxo(sessionFactory, transaction.transactionId);
        blockchainUtxoDao.create(sessionFactory, blockchainUtxo);

        return transaction;
    }
}
