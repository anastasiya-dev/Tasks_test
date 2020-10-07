package by.it.academy.management;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UtxoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.ArrayList;

@Service
public class WalletManagement {

    @Autowired
    UtxoService utxoService;
    @Autowired
    UtxoManagement utxoManagement;
    @Autowired
    TransactionManagement transactionManagement;

    private static final Logger log = LoggerFactory.getLogger(WalletManagement.class);

    public float getBalance(Wallet wallet) {
        log.info("Calculating balance for wallet " + wallet.getWalletId());
        float total = 0;
        ArrayList<Utxo> UTXOs = utxoService.findAllUTXOs();
        for (Utxo UTXO : UTXOs) {
            if (utxoManagement.isMine(UTXO, wallet)) { //if output belongs to me ( if coins belong to me )
                total += UTXO.getValue();
            }
        }
        log.info("Result: " + total);
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(Wallet wallet, PublicKey recipient, float value, Transaction transaction) {

        ArrayList<Utxo> allUTXOs = utxoService.findAllUTXOs();
        ArrayList<Utxo> walletUTXOs = new ArrayList<>();
        for (Utxo utxo : allUTXOs) {
            if (utxoManagement.isMine(utxo, wallet)) {
                walletUTXOs.add(utxo);
            }
        }
        float total = 0;
        for (Utxo UTXO : walletUTXOs) {
            log.info("Wallet " + wallet.getWalletId() + " is paying for transaction " + transaction.getTransactionId());
            log.info(String.valueOf(UTXO));
            total += UTXO.getValue();
            Utxo utxoFromChain = utxoService.findUTXOById(UTXO.getUtxoId());
            utxoFromChain.setOutputTransactionId(transaction.getTransactionId());
            utxoFromChain.setWalletId("");
            utxoService.updateUtxo(utxoFromChain);
            if (total > value) break;
        }
        transactionManagement.processTransaction(transaction);
        return transaction;
    }
}
