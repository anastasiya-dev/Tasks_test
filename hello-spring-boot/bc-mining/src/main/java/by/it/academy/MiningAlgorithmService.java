package by.it.academy;

import by.it.academy.pojo.MiningAlgorithm;
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
public class MiningAlgorithmService {

//    @Autowired
//    MiningSession miningSession;
    @Autowired
    BlockGenerator blockGenerator;
    @Autowired
    Consistency consistency;
    @Autowired
    MiningSessionService miningSessionService;
//    @Autowired
//    MiningAlgorithm miningAlgorithm;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(MiningAlgorithmService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int difficulty = ApplicationConfiguration.difficulty;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    private void starting(String miningSessionId){
//        miningAlgorithm.setMiningSessionId(miningSessionId);
//    }

    @SneakyThrows
    public void run(MiningSession miningSession) {

//        this.setName(String.valueOf(miningAlgorithm));
//        System.out.println("Id mining: " + miningAlgorithm);
        miningSession.setSessionStart(LocalDateTime.now().format(formatter));
        logger.info("Run method invocation");
        blockGenerator.generateBlockchain(difficulty, miningSession);
        if (consistency.isChainValid(difficulty)) {
            logger.info("Blockchain confirmed by session " + miningSession.getMiningSessionId());
            miningSession.setConsistencyConfirmation(LocalDateTime.now().format(formatter));
            miningSessionService.updateSession(miningSession);
        }
    }
}
