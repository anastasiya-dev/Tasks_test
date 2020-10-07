package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.BlockTemporary;
import by.it.academy.pojo.MiningSession;
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
public class BlockTemporaryService {

    @Autowired
    BlockTemporaryRepository blockTemporaryRepository;
    @Autowired
    BlockTemporary blockTemporary;
    @Autowired
    BlockService blockService;
    @Autowired
    MiningSessionService miningSessionService;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockTemporaryService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BlockTemporary createBlockTemporary(String miningSessionId) {
        logger.info("Session id in creation: " + miningSessionId);
        MiningSession miningSession = miningSessionService.findById(miningSessionId);
        String previousBlockId = String.valueOf(Integer.parseInt(miningSession.getBlockIdAttempted()) - 1);
        Block previousBlock = blockService.findBlockById(previousBlockId);

        blockTemporary.setBlockTemporaryId(miningSession.getBlockIdAttempted() + "_" + miningSessionId);
        try {
            blockTemporary.setPreviousHash(previousBlock.getHash());
        } catch (NullPointerException e) {
            blockTemporary.setPreviousHash("0");
        }
        blockTemporary.setTimeStamp(new Date().getTime());
        blockTemporary.setMiningSessionId(miningSessionId);
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
                        blockTemporary.getTimeStamp() +
                        blockTemporary.getNonce() +
                        blockTemporary.getMerkleRoot()

        );
        return calculatedHash;
    }

    public BlockTemporary findBlockTemporaryById(String blockId) {
        return blockTemporaryRepository.findById(blockId).get();
    }

    public BlockTemporary updateBlockTemporary(BlockTemporary blockTemporary) {
        logger.info("Updating blockTemporary");
        String id = blockTemporary.getBlockTemporaryId();
        BlockTemporary savedBlockTemporary = blockTemporaryRepository.findById(id).orElseThrow();
        logger.info("Initial: " + savedBlockTemporary);
        logger.info("New: " + blockTemporary);
        if (savedBlockTemporary.equals(blockTemporary)) {
            return savedBlockTemporary;
        } else {
            savedBlockTemporary.setHash(blockTemporary.getHash());
            savedBlockTemporary.setMerkleRoot(blockTemporary.getMerkleRoot());
            savedBlockTemporary.setNonce(blockTemporary.getNonce());
            savedBlockTemporary.setBlockId(blockTemporary.getBlockId());
            blockTemporaryRepository.save(savedBlockTemporary);
        }
        return blockTemporaryRepository.findById(id).orElseThrow();
    }

    public BlockTemporary findByMiningSessionId(String miningSessionId) {
        return blockTemporaryRepository.findByMiningSessionId(String.valueOf(miningSessionId));
    }
}
