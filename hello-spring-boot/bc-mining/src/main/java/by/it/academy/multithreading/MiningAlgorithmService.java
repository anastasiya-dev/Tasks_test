package by.it.academy.multithreading;

import by.it.academy.ApplicationConfiguration;
import by.it.academy.BlockGenerator;
import by.it.academy.Consistency;
import by.it.academy.pojo.Block;
import by.it.academy.pojo.MiningSession;
import by.it.academy.service.BlockService;
import by.it.academy.service.MiningSessionService;
import by.it.academy.util.LoggerUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class MiningAlgorithmService {

    @Autowired
    BlockGenerator blockGenerator;
    @Autowired
    Consistency consistency;
    @Autowired
    MiningSessionService miningSessionService;
    @Autowired
    BlockService blockService;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(MiningAlgorithmService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int difficulty = ApplicationConfiguration.DIFFICULTY;
    DateTimeFormatter formatter = ApplicationConfiguration.FORMATTER;

    @SneakyThrows
    public void run(MiningSession miningSession) {
//        ArrayList<Block> allBlocks = blockService.findAllBlocks();
//        miningSession.setBlockIdAttempted(String.valueOf(allBlocks.size()));
        miningSession.setSessionStart(LocalDateTime.now().format(formatter));
        miningSessionService.updateSession(miningSession);
        logger.info("Run method invocation for session: " + miningSession.getMiningSessionId());
        blockGenerator.generateBlockchain(difficulty, miningSession);
        if (consistency.isChainValid(difficulty)) {
            logger.info("Blockchain confirmed by session " + miningSession.getMiningSessionId());
            MiningSession result = miningSessionService.findById(miningSession.getMiningSessionId());
            result.setConsistencyConfirmation(LocalDateTime.now().format(formatter));
            miningSessionService.updateSession(result);
        }
    }
}
