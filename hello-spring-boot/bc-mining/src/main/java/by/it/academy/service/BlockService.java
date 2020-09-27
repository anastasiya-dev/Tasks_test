package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.repository.BlockRepository;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class BlockService {

    @Autowired
    BlockRepository blockRepository;
    @Autowired
    Block block;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Block Constructor.
    public Block createBlock(String previousHash, String miningSessionId) {
        ArrayList<Block> allBlocks = findAllBlocks();
        if (allBlocks.isEmpty()) {
            block.setBlockId("0" + "_" + miningSessionId);
        } else {
            block.setBlockId(String.valueOf(allBlocks.size()) + "_" + miningSessionId);
        }

        block.setPreviousHash(previousHash);
        block.setTimeStamp(new Date().getTime());
        block.setHash(calculateHash(block));
        Block saved = blockRepository.save(block);
        logger.info("Creating block: " + block);

        return saved;
    }

    public String calculateHash(Block block) {

        String calculatedhash = StringUtil.applySha256(
                block.getPreviousHash() +
                        Long.toString(block.getTimeStamp()) +
                        Integer.toString(block.getNonce()) +
                        block.getMerkleRoot()

        );
//        logger.info("calculating hash for block: " + block.getBlockId());
//        logger.info("Result: " + calculatedhash);
        return calculatedhash;
    }

    public ArrayList<Block> findAllBlocks() {
        logger.info("Extracting all blocks");
        return (ArrayList<Block>) blockRepository.findAll();
    }

    public Block findBlockById(String blockId) {
        logger.info("Extracting block by id: " + blockId);
        try {
            return blockRepository.findById(blockId).get();
        } catch (NoSuchElementException e) {
            logger.warning("No such block exists");
            return null;
        }
    }

//    public ArrayList<Block> findBlockByMiningSessionId(String minerId) {
//        logger.info("Extracting block by miner id: " + minerId);
//        try {
//            return blockRepository.findByMinerId(minerId);
//        } catch (NoSuchElementException e) {
//            logger.warning("No such block exists");
//            return null;
//        }
//    }

    public boolean saveBlock(Block block) {
        logger.info("Saving block: " + block);
        blockRepository.save(block);
        return true;
    }
}
