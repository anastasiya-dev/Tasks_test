package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockService {

    @Autowired
    TransactionService transactionService;

    //Add transactions to this block
    public boolean addTransaction(Block block, Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((block.getPreviousHash() != "0")) {
            if ((transactionService.processTransaction(transaction) != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        block.transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}