package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.UtxoDao;
import by.it.academy.repository.WalletDao;
import by.it.academy.util.UtxoUtil;
import org.hibernate.SessionFactory;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class WalletService {

    WalletDao walletDao = new WalletDao();
    UtxoDao utxoDao = new UtxoDao();
    UtxoService utxoService = new UtxoService();

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
        ArrayList<Utxo> UTXOs = (ArrayList<Utxo>) utxoDao.findAll(sessionFactory, "");
        for (Utxo UTXO : UTXOs) {
            if (UtxoUtil.isMine(UTXO, wallet)) { //if output belongs to me ( if coins belong to me )
                total += UTXO.value;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(SessionFactory sessionFactory, Wallet wallet, PublicKey recipient, float value, Transaction transaction) {

        float total = 0;
        for (Utxo UTXO : wallet.UTXOs) {
            total += UTXO.value;
            Utxo utxoFromChain = (Utxo) utxoDao.findById(sessionFactory, UTXO.utxoId);
            utxoFromChain.setOutputTransactionId(transaction.transactionId);
            utxoFromChain.setWallet(null);
            utxoDao.create(sessionFactory, utxoFromChain);
            Utxo utxoIntoChain = utxoService.createBcUtxo(sessionFactory, transaction.transactionId);
            utxoDao.create(sessionFactory, utxoIntoChain);
            wallet.UTXOs.remove(UTXO);
            createNewWallet(sessionFactory, wallet);
            if (total > value) break;
        }
        return transaction;
    }
}
