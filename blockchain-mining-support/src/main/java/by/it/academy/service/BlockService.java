package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BlockDao;
import by.it.academy.util.BlockUtil;
import by.it.academy.util.StringUtil;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class BlockService {

    TransactionService transactionService = new TransactionService();
    BlockDao blockDao = new BlockDao();

    //Add transactions to this block
    public boolean addTransaction(SessionFactory sessionFactory, Block block, Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((block.getPreviousHash() != "0")) {
            if ((transactionService.processTransaction(sessionFactory, transaction) != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        block.getTransactions().add(transaction);
        blockDao.create(sessionFactory, block);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

    public void mineBlock(SessionFactory sessionFactory, Block block, int difficulty) {
        List<Transaction> transactions = block.getTransactions();
        List<Transaction> blockTransactions = new ArrayList<>();
        blockTransactions.addAll(transactions);
        block.merkleRoot = StringUtil.getMerkleRoot((ArrayList<Transaction>) blockTransactions);
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"

        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(BlockUtil.calculateHash(block));

        }
        blockDao.create(sessionFactory, block);
        System.out.println("Block Mined!!! : " + block.getHash());
    }
}
