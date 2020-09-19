package by.it.academy.management;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.service.TransactionService;
import by.it.academy.service.UtxoService;
import by.it.academy.support.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionManagement {

    @Autowired
    TransactionService transactionService;
    @Autowired
    UtxoService utxoService;

    public ArrayList<Transaction> getAllForWallet(String walletId, boolean createdStatusFlag) {
        ArrayList<Transaction> transactionsAll = transactionService.findAllTransactions();
        ArrayList<Transaction> transactionsForWallet = new ArrayList<>();
        for (Transaction transaction : transactionsAll) {
            if (!transaction.getRecipientId().equals(transaction.getSenderId())) {
                if ((transaction.getTransactionStatus().equals(TransactionStatus.CREATED) == createdStatusFlag)) {
                    if (transaction.getRecipientId().equals(walletId)) {
                        transactionsForWallet.add(transaction);
                    }
                    if (transaction.getSenderId().equals(walletId)) {
                        transaction.setValue(transaction.getValue() * (float) -1);
                        transactionsForWallet.add(transaction);
                    }
                }
            }
        }
        return transactionsForWallet;
    }


    public float getOutputsValue(Transaction transaction) {
        float total = 0;
        ArrayList<Utxo> BcUTXOs = (ArrayList<Utxo>) utxoService.findAllUTXOs();
        for (Utxo i : BcUTXOs) {
            if (i.getOutputTransactionId() != null && i.getOutputTransactionId().equals(transaction.getTransactionId())) {
                total += i.getValue();
            }
        }
        return total;
    }

    //Returns true if new transaction could be created.
    public void processTransaction(Transaction transaction) {

        float leftOver = getOutputsValue(transaction) - transaction.getValue(); //get value of inputs then the left over change:

        Utxo utxoActual = utxoService.createUtxo(transaction.getTransactionId());
        utxoActual.setWalletId(transaction.getRecipientId());
        utxoService.saveUtxo(utxoActual);

        Utxo utxoChange = utxoService.createUtxo(transaction.getTransactionId());
        utxoChange.setValue(leftOver);
        utxoChange.setWalletId(transaction.getSenderId());
        utxoService.saveUtxo(utxoChange);
    }
}
