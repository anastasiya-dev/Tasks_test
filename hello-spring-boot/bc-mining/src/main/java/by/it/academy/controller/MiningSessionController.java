package by.it.academy.controller;

import by.it.academy.ApplicationConfiguration;
import by.it.academy.pojo.MiningSession;
import by.it.academy.pojo.Transaction;
import by.it.academy.service.*;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

@RestController
public class MiningSessionController {

    @Autowired
    MiningSessionService miningSessionService;
    @Autowired
    BlockService blockService;
    @Autowired
    BlockTemporaryService blockTemporaryService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionPackageService transactionPackageService;

    int maxQuantity = ApplicationConfiguration.MAX_QUANTITY;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(MiningSessionController.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/new-session")
    public ResponseEntity<MiningSession> startMiningSession(@RequestBody MiningSession miningSession) {
        logger.info("Received inquiry for mining session for wallet id: " + miningSession.getWalletId());
        miningSession.setBlockIdAttempted(String.valueOf(blockService.findAllBlocks().size()));
        boolean result = miningSessionService.saveMiningSession(miningSession);
        blockTemporaryService.createBlockTemporary(miningSession.getMiningSessionId());
        formTransactionsSetForSession(miningSessionService.findById(miningSession.getMiningSessionId()));
        return (result ?
                new ResponseEntity<>(miningSession, HttpStatus.OK) :
                new ResponseEntity<>(miningSession, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private void formTransactionsSetForSession(MiningSession miningSession) {
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
