package by.it.academy.service;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import by.it.academy.util.BlockchainUtxoUtil;
import by.it.academy.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    @Value("#{walletDao}")
    BaseDao walletDao;

    @Autowired
    @Value("#{blockchainUtxoDao}")
    BaseDao blockchainUtxoDao;

    @Autowired
    TransactionService transactionService;

    @Autowired
    BlockchainUtxoService blockchainUtxoService;

    public boolean createNewWallet(Wallet wallet) {
        walletDao.create(wallet);
        return true;
    }

    public List<Wallet> getAll(String userId) {
        List<Wallet> all = walletDao.findAll("");
        List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : all) {
            if (wallet.getUser().getUserId().equals(userId)) {
                wallets.add(wallet);
            }
        }
        return wallets;
    }

    public Wallet findWalletById(String id) {
        return (Wallet) walletDao.findById(id);
    }

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance(Wallet wallet) {
        float total = 0;
        ArrayList<BlockchainUtxo> UTXOs = (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll("");
        for (BlockchainUtxo UTXO : UTXOs) {
            if (BlockchainUtxoUtil.isMine(UTXO, wallet.publicKey)) { //if output belongs to me ( if coins belong to me )
//                wallet.UTXOs.add(UTXO); //add it to our list of unspent transactions.
//                walletDao.create(wallet);
                total += UTXO.value;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(Wallet wallet, PublicKey recipient, float value, Transaction transaction) {

        System.out.println("send funds received transaction: " + transaction);
        float total = 0;
        for (BlockchainUtxo UTXO : wallet.UTXOs) {
            total += UTXO.value;
            BlockchainUtxo utxoFromChain = (BlockchainUtxo) blockchainUtxoDao.findById(UTXO.blockchainUtxoId);
            utxoFromChain.setOutputTransactionId(transaction.transactionId);
            utxoFromChain.setWallet(null);
            blockchainUtxoDao.create(utxoFromChain);
            BlockchainUtxo utxoIntoChain = blockchainUtxoService.createBcUtxo(transaction.transactionId);
            blockchainUtxoDao.create(utxoIntoChain);
            wallet.UTXOs.remove(UTXO);
            createNewWallet(wallet);
            if (total > value) break;
        }
        return transaction;
    }
}
