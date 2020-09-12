package by.it.academy.service;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BlockchainUtxoDao;
import by.it.academy.repository.WalletDao;
import by.it.academy.util.BlockchainUtxoUtil;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

//@Service
public class WalletService {

    //    @Autowired
//    @Value("#{walletDao}")
    WalletDao walletDao = new WalletDao();

    //    @Autowired
//    @Value("#{blockchainUtxoDao}")
    BlockchainUtxoDao blockchainUtxoDao = new BlockchainUtxoDao();

    public boolean createNewWallet(SessionFactory sessionFactory, Wallet wallet) {
        walletDao.create(sessionFactory, wallet);
        return true;
    }

    //    public List<Wallet> getAll(String userId) {
//        List<Wallet> all = walletDao.findAll("");
//        List<Wallet> wallets = new ArrayList<>();
//        for (Wallet wallet : all) {
//            if (wallet.getUser().getUserId().equals(userId)) {
//                wallets.add(wallet);
//            }
//        }
//        return wallets;
//    }
//
    public Wallet findWalletById(SessionFactory sessionFactory, String id) {
        return (Wallet) walletDao.findById(sessionFactory, id);
    }

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance(SessionFactory factory, Wallet wallet) {
        float total = 0;
        ArrayList<BlockchainUtxo> UTXOs = (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll(factory,"");
        for (BlockchainUtxo UTXO : UTXOs) {
            if (BlockchainUtxoUtil.isMine(UTXO, wallet.publicKey)) { //if output belongs to me ( if coins belong to me )
                wallet.UTXOs.add(UTXO); //add it to our list of unspent transactions.
                walletDao.create(factory, wallet);
                total += UTXO.value;
            }
        }
        return total;
    }

//    //Generates and returns a new transaction from this wallet.
//    public Transaction sendFunds(Wallet wallet, PublicKey recipient, float value) {
//        if (getBalance(wallet) < value) { //gather balance and check funds.
//            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
//            return null;
//        }
//        //create array list of inputs
//        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
//
//        float total = 0;
//        for (BlockchainUtxo UTXO : wallet.UTXOs) {
//            total += UTXO.value;
//            inputs.add(TransactionInputUtil.createTransactionInput(UTXO.getBlockchainUtxoId()));
//            if (total > value) break;
//        }
//
//        Transaction newTransaction = TransactionUtil.createTransaction(wallet.publicKey, recipient, value, inputs);
//        TransactionUtil.generateSignature(newTransaction, wallet.privateKey);
//
//        for (TransactionInput input : inputs) {
//            wallet.UTXOs.remove(input.transactionOutputId);
//        }
//        return newTransaction;
//    }
}
