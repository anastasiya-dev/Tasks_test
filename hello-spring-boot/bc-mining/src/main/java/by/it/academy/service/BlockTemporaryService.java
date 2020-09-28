package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.BlockTemporary;
import by.it.academy.repository.BlockTemporaryRepository;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

@Service
//@Transactional
public class BlockTemporaryService {

    @Autowired
    BlockTemporaryRepository blockTemporaryRepository;
    @Autowired
    BlockTemporary blockTemporary;
    @Autowired
    BlockService blockService;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockTemporaryService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public BlockTemporary createBlockTemporary(String previousHash, String miningSessionId) {
        ArrayList<Block> allBlocks = blockService.findAllBlocks();
        if (allBlocks.isEmpty()) {
            blockTemporary.setBlockId("0" + "_" + miningSessionId);
        } else {
            blockTemporary.setBlockId(String.valueOf(allBlocks.size()) + "_" + miningSessionId);
        }

        blockTemporary.setPreviousHash(previousHash);
        blockTemporary.setTimeStamp(new Date().getTime());
        blockTemporary.setHash(calculateHash(blockTemporary));
        BlockTemporary saved = blockTemporaryRepository.save(blockTemporary);
        logger.info("Creating block temporary: " + blockTemporary);
        return saved;
    }

    private ArrayList<BlockTemporary> findAllBlocksTemporary() {
        logger.info("Extracting all blocks temporary");
        return (ArrayList<BlockTemporary>) blockTemporaryRepository.findAll();
    }


    public String calculateHash(BlockTemporary blockTemporary) {

        String calculatedHash = StringUtil.applySha256(
                blockTemporary.getPreviousHash() +
                        Long.toString(blockTemporary.getTimeStamp()) +
                        Integer.toString(blockTemporary.getNonce()) +
                        blockTemporary.getMerkleRoot()

        );
        return calculatedHash;
    }

    public BlockTemporary findBlockTemporaryById(String blockId) {
        return blockTemporaryRepository.findById(blockId).get();
    }

    //    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public BlockTemporary updateBlockTemporary(BlockTemporary blockTemporary) {
        logger.info("Updating blockTemporary");
        String id = blockTemporary.getBlockId();
        BlockTemporary savedBlockTemporary = blockTemporaryRepository.findById(id).orElseThrow();
        logger.info("Initial: " + savedBlockTemporary);
        logger.info("New: " + blockTemporary);
        if (savedBlockTemporary.equals(blockTemporary)) {
            return savedBlockTemporary;
        } else {
            savedBlockTemporary.setHash(blockTemporary.getHash());
            savedBlockTemporary.setMerkleRoot(blockTemporary.getMerkleRoot());
            savedBlockTemporary.setNonce(blockTemporary.getNonce());
            blockTemporaryRepository.save(savedBlockTemporary);
        }
        return blockTemporaryRepository.findById(id).orElseThrow();
    }
}
