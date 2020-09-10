package by.it.academy.util;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;

import java.util.Date;

public class BlockUtil {
    //Block Constructor.
    public static Block createBlock(String previousHash) {
        Block block = new Block();
        block.setPreviousHash(previousHash);
        block.setTimeStamp(new Date().getTime());
        block.setHash(calculateHash(block));
        return block;
    }

    public static String calculateHash(Block block) {
        String calculatedhash = StringUtil.applySha256(
                block.getPreviousHash() +
                        Long.toString(block.getTimeStamp()) +
                        Integer.toString(block.getNonce()) +
                        block.getMerkleRoot()
        );
        return calculatedhash;
    }

    public static void mineBlock(Block block, int difficulty) {
        block.merkleRoot = StringUtil.getMerkleRoot(block.transactions);
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(calculateHash(block));
        }
        System.out.println("Block Mined!!! : " + block.getHash());
    }

    //Add transactions to this block
    public static boolean addTransaction(Block block, Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((block.getPreviousHash() != "0")) {
            if ((TransactionUtil.processTransaction(transaction) != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        block.transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}
