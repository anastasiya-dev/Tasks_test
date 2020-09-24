package by.it.academy;

import by.it.academy.management.BlockManagement;
import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.service.BlockService;
import by.it.academy.service.TransactionService;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;

@Service
public class BlockGenerator {

    public int maxQuantity = 5;

    @Autowired
    BlockService blockService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    BlockManagement blockManagement;

    public void generateBlockchain(int difficulty) throws IOException {

        Logger logger = LoggerUtil.startLogging(BlockGenerator.class.getName());
        logger.info("Starting block generation task");

        ArrayList<Block> blockchain = blockService.findAllBlocks();
        blockchain.sort(Comparator.comparingLong(Block::getTimeStamp));

        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        ArrayList<Transaction> transactionsToAdd = formTransactionsPackage(allTransactions);

        if (transactionsToAdd.isEmpty()) {
            logger.info("No transactions for block found");
        } else {
            Block block = blockService.createBlock(blockchain.get(blockchain.size() - 1).getHash());
            for (Transaction transaction : transactionsToAdd) {
                logger.info("Adding to block " + block.getBlockId());
                logger.info(transaction.toString());
                blockManagement.addTransaction(block, transaction);
            }
            logger.info("Mining block (id, difficulty): " + block.getBlockId() + ", " + difficulty);
            blockManagement.mineBlock(block, difficulty);
        }
    }

    private ArrayList<Transaction> formTransactionsPackage(ArrayList<Transaction> allTransactions) {
        ArrayList<Transaction> transactionsToAdd = new ArrayList<>();
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
