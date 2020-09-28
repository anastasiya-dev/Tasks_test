package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.BlockTemporary;
import by.it.academy.repository.BlockRepository;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
//@Transactional
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

    public Block createBlock(String previousHash, String miningSessionId) {
        ArrayList<Block> allBlocks = findAllBlocks();
        if (allBlocks.isEmpty()) {
            block.setBlockId("0" + "_" + miningSessionId);
        } else {
            block.setBlockId(allBlocks.size() + "_" + miningSessionId);
        }

        block.setPreviousHash(previousHash);
        block.setTimeStamp(new Date().getTime());
        block.setHash(calculateHash(block));
        logger.info("Creating block: " + block);
        return block;
    }

    public String calculateHash(Block block) {

        String calculatedHash = StringUtil.applySha256(
                block.getPreviousHash() +
                        Long.toString(block.getTimeStamp()) +
                        Integer.toString(block.getNonce()) +
                        block.getMerkleRoot()

        );
        return calculatedHash;
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

    public ArrayList<Block> findBlockByMinerId(String minerId) {
        logger.info("Extracting all blocks by miner id: " + minerId);
        List<Block> allBlocks = (List<Block>) blockRepository.findAll();
        ArrayList<Block> minerBlocks = new ArrayList<>();
        for (Block blockIterator : allBlocks) {
            if (blockIterator.getMinerId().equals(minerId)) {
                minerBlocks.add(blockIterator);
            }
        }
        return minerBlocks;
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public boolean saveBlock(Block block) {
        logger.info("Saving block: " + block);
        blockRepository.save(block);
        return true;
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Block transformFromTemporary(BlockTemporary blockTemporary) {
        block.setBlockId(blockTemporary.getBlockId());
        block.setMerkleRoot(blockTemporary.getMerkleRoot());
        block.setHash(blockTemporary.getHash());
        block.setNonce(blockTemporary.getNonce());
        block.setTimeStamp(blockTemporary.getTimeStamp());
        block.setPreviousHash(blockTemporary.getPreviousHash());
        Block saved = blockRepository.save(block);
        return saved;
    }
}
