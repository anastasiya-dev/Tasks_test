package by.it.academy;

import by.it.academy.management.BlockManagement;
import by.it.academy.pojo.BlockTemporary;
import by.it.academy.pojo.MiningSession;
import by.it.academy.service.BlockService;
import by.it.academy.service.BlockTemporaryService;
import by.it.academy.service.TransactionService;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class BlockGenerator {

    @Autowired
    BlockManagement blockManagement;
    @Autowired
    BlockTemporaryService blockTemporaryService;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockGenerator.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateBlockchain(int difficulty, MiningSession miningSession) {
        logger.info("Starting block generation task");
        BlockTemporary blockTemporary = blockTemporaryService.findByMiningSessionId(miningSession.getMiningSessionId());
        try {
            logger.info("Mining block (id, difficulty, miningSession): "
                    + blockTemporary.getBlockId() + ", "
                    + difficulty + ", "
                    + miningSession.getMiningSessionId());
            blockManagement.mineBlock(blockTemporary, difficulty, miningSession);
        } catch (NullPointerException e) {
            logger.warning("No transaction for block were found");
        }
    }
}
