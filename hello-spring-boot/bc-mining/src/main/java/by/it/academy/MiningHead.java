package by.it.academy;

import by.it.academy.pojo.MiningAlgorithm;
import by.it.academy.pojo.MiningSession;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class MiningHead implements ApplicationContextAware {

    Logger logger;

    ApplicationContext context;

    {
        try {
            logger = LoggerUtil.startLogging(MiningAlgorithmService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void miningRunner(ArrayList<MiningSession> allMiningSessionsByStatus) {
        for (MiningSession miningSession : allMiningSessionsByStatus) {
            logger.info("Starting thread for mining session " + miningSession.getMiningSessionId());
            MiningAlgorithm taskJob = new MiningAlgorithm(miningSession.getMiningSessionId());
            Thread taskThread = new Thread(taskJob);
            context.getAutowireCapableBeanFactory().autowireBean(taskJob);
            taskThread.start();
//                    applicationContext.getAutowireCapableBeanFactory().autowireBean(taskJob);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
