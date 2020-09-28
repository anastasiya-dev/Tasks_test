package by.it.academy;

import by.it.academy.management.BlockManagement;
import by.it.academy.pojo.Block;
import by.it.academy.pojo.BlockTemporary;
import by.it.academy.pojo.MiningSession;
import by.it.academy.pojo.Transaction;
import by.it.academy.service.BlockService;
import by.it.academy.service.BlockTemporaryService;
import by.it.academy.service.TransactionService;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

@Service
public class BlockGenerator {

    public int maxQuantity = 3;

    @Autowired
    BlockService blockService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    BlockManagement blockManagement;
    @Autowired
    BlockTemporaryService blockTemporaryService;

    public void generateBlockchain(int difficulty, MiningSession miningSession) throws IOException {

        Logger logger = LoggerUtil.startLogging(BlockGenerator.class.getName());
        logger.info("Starting block generation task");

        ArrayList<Block> blockchain = blockService.findAllBlocks();
        blockchain.sort(Comparator.comparingLong(Block::getTimeStamp));

        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        ArrayList<Transaction> transactionsToAdd = formTransactionsSetForBlock(allTransactions);

        if (transactionsToAdd.isEmpty()) {
            logger.info("No transactions for block found");
            return;
        } else {
            BlockTemporary blockTemporary = blockTemporaryService.createBlockTemporary(blockchain.get(blockchain.size() - 1).getHash(), miningSession.getMiningSessionId());

            for (Transaction transaction : transactionsToAdd) {
                logger.info("Adding to block " + blockTemporary.getBlockId());
                logger.info(transaction.getTransactionId());
                blockManagement.addTransaction(blockTemporary, transaction);
            }
            logger.info("Mining block (id, difficulty, miningSession): "
                    + blockTemporary.getBlockId() + ", "
                    + difficulty + ", "
                    + miningSession.getMiningSessionId());
            blockManagement.mineBlock(blockTemporary, difficulty, miningSession, blockchain);
        }
    }

    private ArrayList<Transaction> formTransactionsSetForBlock(ArrayList<Transaction> allTransactions) {
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
        return transactionsToAdd;
    }
}
