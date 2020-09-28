package by.it.academy;

import by.it.academy.management.TransactionManagement;
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
public class Consistency {

    @Autowired
    BlockService blockService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionManagement transactionManagement;

    public boolean isChainValid(int difficulty) throws IOException {

        Logger logger = LoggerUtil.startLogging(Consistency.class.getName());
        logger.info("Starting validation");

        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        ArrayList<Block> blockchain = blockService.findAllBlocks();
        blockchain.sort(Comparator.comparingLong(Block::getTimeStamp));

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            ArrayList<Transaction> blockTransactions = new ArrayList<>();
            for (Transaction transaction : allTransactions) {
                if (!transaction.getTransactionStatus().equals(TransactionStatus.CREATED)) {
                    if (transaction.getBlockId().equals(currentBlock.getBlockId())) {
                        blockTransactions.add(transaction);
                    }
                }
            }
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(blockService.calculateHash(currentBlock))) {
                logger.warning("Current Hashes not equal for block " + currentBlock.getBlockId());
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                logger.warning("Previous Hashes not equal for block " + currentBlock.getBlockId());
                return false;
            }
            //check if hash is solved
            if (!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)) {
                logger.warning("This block hasn't been mined " + currentBlock.getBlockId());
                return false;
            }

            //loop thru blockchains transactions:
            for (int t = 0; t < blockTransactions.size(); t++) {
                Transaction currentTransaction = blockTransactions.get(t);
                if (currentTransaction.getBlockId() == null) {
                    break;
                }
                if (!transactionManagement.verifySignature(currentTransaction)) {
                    logger.warning("Signature on Transaction is Invalid: " + currentTransaction.getTransactionId());
                    return false;
                }
                if (transactionManagement.getInputsValue(currentTransaction) !=
                        transactionManagement.getOutputsValue(currentTransaction)) {
                    logger.warning("Inputs are note equal to outputs on Transaction: " + currentTransaction.getTransactionId());
                    return false;
                }
            }
        }
        logger.info("Blockchain is valid");
        return true;
    }
}
