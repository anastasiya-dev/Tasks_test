package by.it.academy;

import by.it.academy.pojo.MiningSession;
import by.it.academy.service.MiningSessionService;
import by.it.academy.util.LoggerUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class MiningAlgorithm extends Thread {

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(MiningAlgorithm.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int difficulty = ApplicationConfiguration.difficulty;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    BlockGenerator blockGenerator;
    @Autowired
    Consistency consistency;
    @Autowired
    MiningSessionService miningSessionService;

    MiningSession miningSession;

    public MiningAlgorithm(MiningSession miningSession) {
        super();
        logger.info("Starting thread for session " + miningSession.getMiningSessionId());
        this.miningSession = miningSession;
        this.miningSession.setSessionStart(LocalDateTime.now().format(formatter));
    }

    @SneakyThrows
    @Override
    public void run() {
        logger.info("Run method invocation");
        blockGenerator.generateBlockchain(difficulty, miningSession);
        if (consistency.isChainValid(difficulty)) {
            logger.info("Blockchain confirmed by session " + miningSession.getMiningSessionId());
            miningSession.setConsistencyConfirmation(LocalDateTime.now().format(formatter));
            miningSessionService.updateSession(miningSession);
        }
    }
}
