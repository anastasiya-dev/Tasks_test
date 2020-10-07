package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.BlockTemporary;
import by.it.academy.repository.BlockRepository;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public String calculateHash(Block block) {

        String calculatedHash = StringUtil.applySha256(
                block.getPreviousHash() +
                        block.getTimeStamp() +
                        block.getNonce() +
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

    public Block transformFromTemporary(BlockTemporary blockTemporary) {
        block.setBlockId(blockTemporary.getBlockId());
        block.setMerkleRoot(blockTemporary.getMerkleRoot());
        block.setHash(blockTemporary.getHash());
        block.setNonce(blockTemporary.getNonce());
        block.setTimeStamp(blockTemporary.getTimeStamp());
        block.setPreviousHash(blockTemporary.getPreviousHash());
        Block saved = blockRepository.save(block);
        logger.info("Transformed temporary to actual: " + saved);
        return saved;
    }

    public Block updateBlock(Block block) {
        logger.info("Updating block");
        String id = block.getBlockId();
        Block savedBlock = blockRepository.findById(id).orElseThrow();
        logger.info("Initial: " + savedBlock);
        logger.info("New: " + block);
        if (savedBlock.equals(block)) {
            return savedBlock;
        } else {
            savedBlock.setPreviousHash(block.getPreviousHash());
            savedBlock.setMinerId(block.getMinerId());
            savedBlock.setTimeStamp(block.getTimeStamp());
            savedBlock.setNonce(block.getNonce());
            savedBlock.setHash(block.getHash());
            savedBlock.setMerkleRoot(block.getMerkleRoot());
            savedBlock.setMiningSessionId(block.getMiningSessionId());

            blockRepository.save(savedBlock);
        }
        return blockRepository.findById(id).orElseThrow();
    }
}
