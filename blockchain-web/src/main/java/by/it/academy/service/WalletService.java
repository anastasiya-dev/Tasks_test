package by.it.academy.service;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import by.it.academy.util.UtxoUtil;
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
    @Value("#{utxoDao}")
    BaseDao utxoDao;

    @Autowired
    TransactionService transactionService;

    @Autowired
    UtxoService utxoService;

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
        ArrayList<Utxo> UTXOs = (ArrayList<Utxo>) utxoDao.findAll("");
        for (Utxo UTXO : UTXOs) {
            if (UtxoUtil.isMine(UTXO, wallet)) { //if output belongs to me ( if coins belong to me )
                total += UTXO.value;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(Wallet wallet, PublicKey recipient, float value, Transaction transaction) {

        float total = 0;
        for (Utxo UTXO : wallet.UTXOs) {
            total += UTXO.value;
            Utxo utxoFromChain = (Utxo) utxoDao.findById(UTXO.utxoId);
            utxoFromChain.setOutputTransactionId(transaction.transactionId);
            utxoFromChain.setWallet(null);
            utxoDao.create(utxoFromChain);
            Utxo utxoIntoChain = utxoService.createBcUtxo(transaction.transactionId);
            utxoDao.create(utxoIntoChain);
            wallet.UTXOs.remove(UTXO);
            createNewWallet(wallet);
            if (total > value) break;
        }
        return transaction;
    }
}
