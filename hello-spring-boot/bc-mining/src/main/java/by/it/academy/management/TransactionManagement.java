package by.it.academy.management;

import by.it.academy.ApplicationConfiguration;
import by.it.academy.pojo.MiningSession;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.service.TransactionPackageService;
import by.it.academy.service.TransactionService;
import by.it.academy.service.UtxoService;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

@Service
public class TransactionManagement {

    @Autowired
    UtxoService utxoService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionPackageService transactionPackageService;

    int maxQuantity = ApplicationConfiguration.MAX_QUANTITY;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(TransactionManagement.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature(Transaction transaction) {
        logger.info("Verifying signature on transaction " + transaction.getTransactionId());
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    //returns sum of inputs(UTXOs) values
    public float getOutputsValue(Transaction transaction) {
        logger.info("Getting outputs for transaction " + transaction.getTransactionId());
        float total = 0;
        ArrayList<Utxo> UTXOs = utxoService.findAllUTXOs();
        for (Utxo i : UTXOs) {
            if (i.getOutputTransactionId() != null && i.getOutputTransactionId().equals(transaction.getTransactionId())) {
                total += i.getValue();
            }
        }
        logger.info("Result: " + total);
        return total;
    }

    //returns sum of outputs:
    public float getInputsValue(Transaction transaction) {
        logger.info("Getting inputs for transaction " + transaction.getTransactionId());
        float total = 0;
        ArrayList<Utxo> UTXOs = utxoService.findAllUTXOs();
        for (Utxo i : UTXOs) {
            if (i.getInputTransactionId().equals(transaction.getTransactionId())) {
                total += i.getValue();
            }
        }
        logger.info("Result: " + total);
        return total;
    }

    public void formTransactionsSetForSession(MiningSession miningSession) {
        logger.info("Forming transactions set for session: " + miningSession.getMiningSessionId());
        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        ArrayList<Transaction> transactionsToAdd = new ArrayList<>();
        Collections.shuffle(allTransactions);
        int counter = 0;
        for (Transaction transaction : allTransactions) {
            if (transaction.getTransactionStatus().equals(TransactionStatus.CONFIRMED)) {
                transactionsToAdd.add(transaction);
                counter++;
            }
            if (counter >= maxQuantity) {
                break;
            }
        }
        if (transactionsToAdd.isEmpty()) {
            logger.info("No transactions for block found");

        } else {
            logger.info("Session id in generator: " + miningSession.getMiningSessionId());

            for (Transaction transaction : transactionsToAdd) {
                logger.info(transaction.getTransactionId());
                transactionPackageService.createTransactionPackage(miningSession.getBlockIdAttempted(), transaction.getTransactionId(), miningSession.getMiningSessionId());
            }
        }
    }
}
