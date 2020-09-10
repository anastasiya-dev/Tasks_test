package by.it.academy.service;

import by.it.academy.pojo.*;
import by.it.academy.repository.BaseDao;
import by.it.academy.util.StringUtil;
import by.it.academy.util.TransactionOutputUtil;
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
    @Value("#{blockchainUtxoDao}")
    BaseDao blockchainUtxoDao;

    private float minimumTransaction = 0.1f;

    @Autowired
    WalletService walletService;

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

        if (verifySignature(transaction) == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for (TransactionInput i : transaction.inputs) {
            i.transactionOutput = (TransactionOutput) blockchainUtxoDao.findById(i.transactionOutputId);
//                    Blockchain.getUTXOs().get(i.transactionOutputId);
        }

        //check if transaction is valid:
        if (getInputsValue(transaction) < minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue(transaction));
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue(transaction) - transaction.value; //get value of inputs then the left over change:
//        transactionId = calulateHash(transaction);
        transaction.outputs.add(TransactionOutputUtil.createTransactionOutput(transaction.getRecipient(), transaction.getValue(), transaction)); //send value to recipient
        transaction.outputs.add(TransactionOutputUtil.createTransactionOutput(transaction.getSender(), leftOver, transaction)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for (TransactionOutput o : transaction.outputs) {
            BlockchainUtxo blockchainUtxo = new BlockchainUtxo();
            blockchainUtxo.setBlockchainUtxoId(o.id);
            blockchainUtxo.setRecipient(o.recipient);
            blockchainUtxo.setTransactionInputId(o.transactionInput.transactionOutputId);
            blockchainUtxo.setParentTransactionId(o.getTransaction().transactionId);
            blockchainUtxo.setValue(o.value);
            blockchainUtxoDao.create(blockchainUtxo);
//            Blockchain.getUTXOs().put(o.id, o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for (TransactionInput i : transaction.inputs) {
            if (i.transactionOutput == null) continue; //if Transaction can't be found skip it
            BlockchainUtxo blockchainUtxoDel = new BlockchainUtxo();
            blockchainUtxoDel.setBlockchainUtxoId(i.transactionOutput.id);
//            blockchainUtxoDel.setTransactionOutput(i.transactionOutput);

            blockchainUtxoDel.setRecipient(i.transactionOutput.recipient);
            blockchainUtxoDel.setTransactionInputId(i.transactionOutput.transactionInput.transactionOutputId);
            blockchainUtxoDel.setParentTransactionId(i.transactionOutput.getTransaction().transactionId);
            blockchainUtxoDel.setValue(i.transactionOutput.value);

            blockchainUtxoDao.delete(blockchainUtxoDel);
//            Blockchain.getUTXOs().remove(i.transactionOutput.id);
        }

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
        for (TransactionInput i : transaction.inputs) {
            if (i.transactionOutput == null) continue; //if Transaction can't be found skip it
            total += i.transactionOutput.value;
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue(Transaction transaction) {
        float total = 0;
        for (TransactionOutput o : transaction.outputs) {
            total += o.value;
        }
        return total;
    }

}
