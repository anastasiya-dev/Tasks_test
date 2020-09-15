package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.User;
import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import by.it.academy.util.StringUtil;
import by.it.academy.util.UtxoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
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
    @Value("#{userDao}")
    BaseDao userDao;

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

        ArrayList<Utxo> allUTXOs = utxoService.findAllUTXOs();
        ArrayList<Utxo> walletUTXOs = new ArrayList<>();
        for (Utxo utxo : allUTXOs) {
            if (UtxoUtil.isMine(utxo, wallet)){
                walletUTXOs.add(utxo);
            }
        }
        float total = 0;
        for (Utxo UTXO : walletUTXOs) {
            total += UTXO.value;
            Utxo utxoFromChain = (Utxo) utxoDao.findById(UTXO.utxoId);
            utxoFromChain.setOutputTransactionId(transaction.transactionId);
            utxoFromChain.setWalletId(
                    (((ArrayList<User>) userDao.findAll("")).stream()
                            .filter((u -> u.getUserName().equals("Genesis User")))
                            .findFirst().get()).getWallets().get(0).getWalletId()
            );
            utxoDao.update(utxoFromChain);
//            Utxo utxoIntoChain = utxoService.createBcUtxo(transaction.transactionId);
//            utxoDao.create(utxoIntoChain);
//            wallet.UTXOs.remove(UTXO);
            createNewWallet(wallet);
            if (total > value) break;
        }
        return transaction;
    }

    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        generateKeyPair(wallet);
        return wallet;
    }

    private void generateKeyPair(Wallet wallet) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            wallet.privateKey = keyPair.getPrivate();
            wallet.privateKeyString = StringUtil.getStringFromKey(wallet.privateKey);
            wallet.publicKey = keyPair.getPublic();
            wallet.setPublicKeyString(StringUtil.getStringFromKey(wallet.publicKey));
//            ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletDao.findAll("");
//            if (wallets.size() == 0) {
//                wallet.setWalletId("0");
//            } else {
//                wallets.sort(Comparator.comparingInt(w -> Integer.valueOf(w.getWalletId())));
//                wallet.setWalletId(String.valueOf(Integer.valueOf(wallets.get(wallets.size() - 1).getWalletId()) + 1));
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
