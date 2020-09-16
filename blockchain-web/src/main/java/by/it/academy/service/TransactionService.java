package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
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
    @Value("#{utxoDao}")
    BaseDao utxoDao;

    private float minimumTransaction = 0.1f;

    @Autowired
    WalletService walletService;

    @Autowired
    UtxoService utxoService;

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

//    //Returns true if new transaction could be created.
//    public boolean processTransaction(Transaction transaction) {
//
//        ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletDao.findAll("");
//        Wallet sender = new Wallet();
//        Wallet recipient = new Wallet();
//        for (Wallet wallet : wallets) {
//            if (transaction.getSenderString().equals(wallet.getWalletId())) {
//                sender = wallet;
//            }
//            if (transaction.getRecipientString().equals(wallet.getWalletId())) {
//                recipient = wallet;
//            }
//        }
//
//        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoDao.findAll("");
//
//        //check if transaction is valid:
//        if (getOutputsValue(transaction) < minimumTransaction) {
//            System.out.println("#Transaction Outputs too small: " + getOutputsValue(transaction));
//            for (Utxo i : BcUTXOs) {
//                if (i.outputTransactionId.equals(transaction.transactionId)) {
//                    i.setOutputTransactionId("");
//                }
//            }
//            return false;
//        }
//
//        //generate transaction outputs:
//        float leftOver = getOutputsValue(transaction) - transaction.value; //get value of inputs then the left over change:
//        Utxo bcUtxoActual = null;
//        for (Utxo utxo : BcUTXOs) {
//            if (utxo.getInputTransactionId().equals(transaction.transactionId)) {
//                bcUtxoActual = utxo;
//            }
//        }
//        bcUtxoActual.setWalletId(recipient.getWalletId());
////        recipient.getUTXOs().add(bcUtxoActual);
//        Utxo bcUtxoChange = utxoService.createBcUtxo(transaction.transactionId);
//        bcUtxoChange.setValue(leftOver);
//        bcUtxoChange.setWalletId(sender.getWalletId());
//        bcUtxoChange.setRecipient(sender.publicKey);
////        sender.getUTXOs().add(bcUtxoChange);
//        walletService.createNewWallet(sender);
//        walletService.createNewWallet(recipient);
//
//        return true;
//    }

    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    //returns sum of inputs(UTXOs) values
    public float getOutputsValue(Transaction transaction) {
        float total = 0;
        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoDao.findAll("");
        for (Utxo i : BcUTXOs) {
            if (i.outputTransactionId != null && i.outputTransactionId.equals(transaction.transactionId)) {
                total += i.value;
            }
        }
        return total;
    }

    //returns sum of outputs:
    public float getInputsValue(Transaction transaction) {
        float total = 0;
        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoDao.findAll("");
        for (Utxo i : BcUTXOs) {
            if (i.inputTransactionId.equals(transaction.transactionId)) {
                total += i.value;
            }
        }
        return total;
    }

    //Returns true if new transaction could be created.
    public boolean processTransaction(Transaction transactionInProcessing) {
        System.out.println("transaction in the function");
        System.out.println(transactionInProcessing);
        ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletDao.findAll("");
        Wallet sender = walletService.createWallet();
        Wallet recipient = walletService.createWallet();
        for (Wallet walletProcessed : wallets) {
            System.out.println("in the wallet loop");
            System.out.println(walletProcessed);
            if (transactionInProcessing.getSenderString().equals(walletProcessed.getPublicKeyString())) {
                System.out.println("found sender");
                sender = walletProcessed;
            }
            if (transactionInProcessing.getRecipientString().equals(walletProcessed.getPublicKeyString())) {
                System.out.println("found recipient");
                recipient = walletProcessed;
            }
        }

        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoDao.findAll("");

//        //check if transaction is valid:
//        if (getOutputsValue(transactionInProcessing) < minimumTransaction) {
//            System.out.println("#Transaction Outputs too small: " + getOutputsValue(transactionInProcessing));
//            for (Utxo i : BcUTXOs) {
//                if (i.outputTransactionId.equals(transactionInProcessing.transactionId)) {
//                    i.setOutputTransactionId("");
//                }
//            }
//            return false;
//        }

        //generate transaction outputs:
        float leftOver = getOutputsValue(transactionInProcessing) - transactionInProcessing.value; //get value of inputs then the left over change:
//        Utxo bcUtxoActual = null;
//        for (Utxo utxo : BcUTXOs) {
//            if (utxo.getInputTransactionId().equals(transactionInProcessing.transactionId)) {
//                bcUtxoActual = utxo;
//            }
//        }
        Utxo bcUtxoActual = utxoService.createBcUtxo(transactionInProcessing.transactionId);
        bcUtxoActual.setWalletId(recipient.getWalletId());
//        recipient.getUTXOs().add(bcUtxoActual);
        utxoService.createNewUTXO(bcUtxoActual);

        Utxo bcUtxoChange = utxoService.createBcUtxo(transactionInProcessing.transactionId);
        bcUtxoChange.setValue(leftOver);
        bcUtxoChange.setWalletId(sender.getWalletId());
        bcUtxoChange.setRecipient(sender.publicKey);
//        sender.getUTXOs().add(bcUtxoChange);
//        walletService.createNewWallet(sender);
//        walletService.createNewWallet(recipient);
        utxoService.createNewUTXO(bcUtxoChange);

        return true;
    }

}