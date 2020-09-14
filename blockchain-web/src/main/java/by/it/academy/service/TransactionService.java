package by.it.academy.service;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import by.it.academy.util.BlockchainUtxoUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionService {

    @Autowired
    @Value("#{transactionDao}")
    BaseDao transactionDao;

    @Autowired
    @Value("#{walletDao}")
    BaseDao walletDao;

    @Autowired
    @Value("#{blockchainUtxoDao}")
    BaseDao blockchainUtxoDao;

//    @Autowired
//    @Value("#{transactionInputDao}")
//    BaseDao transactionInputDao;

    private float minimumTransaction = 0.1f;

    @Autowired
    WalletService walletService;

    @Autowired
    BlockchainUtxoService blockchainUtxoService;

    public boolean createNewTransaction(Transaction transaction) {
        transactionDao.create(transaction);
        return true;
    }

    public Transaction findTransactionById(String id) {
        return (Transaction) transactionDao.findById(id);
    }

    public ArrayList<Transaction> findAllTransactions() {
        return (ArrayList<Transaction>) transactionDao.findAll("");
    }

    public boolean deleteTransaction(Transaction transaction) {
        return transactionDao.delete(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return (Transaction) transactionDao.update(transaction);
    }


    public ArrayList<Transaction> getAllForWallet(String walletId) {
        ArrayList<Transaction> transactionsAll = (ArrayList<Transaction>) transactionDao.findAll("");
        Wallet wallet = walletService.findWalletById(walletId);
        String publicKey = StringUtil.getStringFromKey(wallet.getPublicKey());
        ArrayList<Transaction> transactionsForWallet = new ArrayList<>();
        for (Transaction transaction : transactionsAll) {
            if (StringUtil.getStringFromKey(transaction.getRecipient())
                    .equals(StringUtil.getStringFromKey(transaction.getSender()))) {
                transactionsForWallet.add(transaction);
                transaction.setValue(transaction.getValue() * Float.valueOf(-1));
                transactionsForWallet.add(transaction);
            } else if (StringUtil.getStringFromKey(transaction.getSender()).equals(publicKey)) {
                transaction.setValue(transaction.getValue() * Float.valueOf(-1));
                transactionsForWallet.add(transaction);
            } else if (StringUtil.getStringFromKey(transaction.getRecipient()).equals(publicKey)) {
                transactionsForWallet.add(transaction);
            }
        }
        return transactionsForWallet;
    }

    //Returns true if new transaction could be created.
    public boolean processTransaction(Transaction transaction) {

        ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletDao.findAll("");
        Wallet sender = new Wallet();
        Wallet recipient = new Wallet();
        for (Wallet wallet : wallets) {
            if (transaction.getSenderString().equals(wallet.getPublicKeyString())) {
                sender = wallet;
            }
            if (transaction.getRecipientString().equals(wallet.getPublicKeyString())) {
                sender = wallet;
            }
        }

        ArrayList<BlockchainUtxo> BcUTXOs = (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll("");

        //check if transaction is valid:
        if (getInputsValue(transaction) < minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue(transaction));
            for (BlockchainUtxo i : BcUTXOs) {
                if (i.outputTransactionId.equals(transaction.transactionId)) {
                    i.setOutputTransactionId("");
                }
            }
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue(transaction) - transaction.value; //get value of inputs then the left over change:
        BlockchainUtxo bcUtxoActual = null;
        for (BlockchainUtxo blockchainUtxo : BcUTXOs) {
            if (blockchainUtxo.getInputTransactionId().equals(transaction.transactionId)) {
                bcUtxoActual = blockchainUtxo;
            }
        }
        bcUtxoActual.setWallet(recipient);
        recipient.getUTXOs().add(bcUtxoActual);
        BlockchainUtxo bcUtxoChange = blockchainUtxoService.createBcUtxo(transaction.transactionId);
        bcUtxoChange.setValue(leftOver);
        bcUtxoChange.setWallet(sender);
        sender.getUTXOs().add(bcUtxoChange);
        walletService.createNewWallet(sender);
        walletService.createNewWallet(recipient);

        return true;
    }

    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    //returns sum of inputs(UTXOs) values
    public float getInputsValue(Transaction transaction) {
        float total = 0;
        ArrayList<BlockchainUtxo> BcUTXOs = (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll("");
        for (BlockchainUtxo i : BcUTXOs) {
            if (i.inputTransactionId.equals(transaction.transactionId)) {
                total += i.value;
            }
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue(Transaction transaction) {
        float total = 0;
        ArrayList<BlockchainUtxo> BcUTXOs = (ArrayList<BlockchainUtxo>) blockchainUtxoDao.findAll("");
        for (BlockchainUtxo i : BcUTXOs) {
            if (i.outputTransactionId.equals(transaction.transactionId)) {
                total += i.value;
            }
        }
        return total;
    }

}
