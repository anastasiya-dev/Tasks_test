package by.it.academy.util;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BlockDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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




}
