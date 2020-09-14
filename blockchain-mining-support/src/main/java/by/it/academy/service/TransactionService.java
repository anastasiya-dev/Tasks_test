package by.it.academy.service;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.UtxoDao;
import by.it.academy.repository.TransactionDao;
import by.it.academy.repository.WalletDao;
import by.it.academy.util.StringUtil;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

public class TransactionService {

    TransactionDao transactionDao = new TransactionDao();
    WalletDao walletDao = new WalletDao();
    UtxoDao utxoDao = new UtxoDao();
    UtxoService utxoService = new UtxoService();

    private float minimumTransaction = 0.1f;

    WalletService walletService = new WalletService();

    public boolean createNewTransaction(SessionFactory sessionFactory, Transaction transaction) {
        transactionDao.create(sessionFactory, transaction);
        return true;
    }

    public Transaction findTransactionById(SessionFactory sessionFactory, String id) {
        return (Transaction) transactionDao.findById(sessionFactory, id);
    }

    public ArrayList<Transaction> findAllTransactions(SessionFactory sessionFactory) {
        return (ArrayList<Transaction>) transactionDao.findAll(sessionFactory, "");
    }

    public boolean deleteTransaction(SessionFactory sessionFactory, Transaction transaction) {
        return transactionDao.delete(sessionFactory, transaction);
    }

    public Transaction updateTransaction(SessionFactory sessionFactory, Transaction transaction) {
        return (Transaction) transactionDao.update(sessionFactory, transaction);
    }


    public ArrayList<Transaction> getAllForWallet(SessionFactory sessionFactory, String walletId) {
        ArrayList<Transaction> transactionsAll = (ArrayList<Transaction>) transactionDao.findAll(sessionFactory, "");
        Wallet wallet = walletService.findWalletById(sessionFactory, walletId);
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
    public boolean processTransaction(SessionFactory sessionFactory, Transaction transaction) {

        ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletDao.findAll(sessionFactory, "");
        Wallet sender = new Wallet();
        Wallet recipient = new Wallet();
        for (Wallet wallet : wallets) {
            if (transaction.getSenderString().equals(wallet.getPublicKeyString())) {
                sender = wallet;
            }
            if (transaction.getRecipientString().equals(wallet.getPublicKeyString())) {
                recipient = wallet;
            }
        }

        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoDao.findAll(sessionFactory, "");

        //check if transaction is valid:
        if (getOutputsValue(sessionFactory, transaction) < minimumTransaction) {
            System.out.println("#Transaction Outputs too small: " + getOutputsValue(sessionFactory, transaction));
            for (Utxo i : BcUTXOs) {
                if (i.outputTransactionId.equals(transaction.transactionId)) {
                    i.setOutputTransactionId("");
                }
            }
            return false;
        }

        //generate transaction outputs:
        float leftOver = getOutputsValue(sessionFactory, transaction) - transaction.value; //get value of inputs then the left over change:
        Utxo bcUtxoActual = null;
        for (Utxo utxo : BcUTXOs) {
            if (utxo.getInputTransactionId().equals(transaction.transactionId)) {
                bcUtxoActual = utxo;
            }
        }
        bcUtxoActual.setWallet(recipient);
        recipient.getUTXOs().add(bcUtxoActual);
        Utxo bcUtxoChange = utxoService.createBcUtxo(sessionFactory, transaction.transactionId);
        bcUtxoChange.setValue(leftOver);
        bcUtxoChange.setWallet(sender);
        bcUtxoChange.setRecipient(sender.publicKey);
        sender.getUTXOs().add(bcUtxoChange);
        walletService.createNewWallet(sessionFactory, sender);
        walletService.createNewWallet(sessionFactory, recipient);

        return true;
    }

    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    //returns sum of inputs(UTXOs) values
    public float getOutputsValue(SessionFactory sessionFactory, Transaction transaction) {
        float total = 0;
        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoDao.findAll(sessionFactory, "");
        for (Utxo i : BcUTXOs) {
            if (i.outputTransactionId != null && i.outputTransactionId.equals(transaction.transactionId)) {
                total += i.value;
            }
        }
        return total;
    }

    //returns sum of outputs:
    public float getInputsValue(SessionFactory sessionFactory, Transaction transaction) {
        float total = 0;
        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoDao.findAll(sessionFactory, "");
        for (Utxo i : BcUTXOs) {
            if (i.inputTransactionId.equals(transaction.transactionId)) {
                total += i.value;
            }
        }
        return total;
    }
}
