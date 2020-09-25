package by.it.academy.service;

import by.it.academy.controller.BlockController;
import by.it.academy.pojo.MiningSession;
import by.it.academy.repository.MiningSessionRepository;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class MiningSessionService {

    static int sequence = 0;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockController.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    MiningSessionRepository miningSessionRepository;

    public boolean createNewMiningSession(MiningSession miningSession) {
        logger.info("Prosessing mining session creaion");
        try {
            miningSessionRepository.findById(miningSession.getMinerId()).get();
            logger.warning("Denied. Already exists");
            return false;
        } catch (NoSuchElementException e) {
            logger.info("Accept");
            miningSession.setMinerId(String.valueOf(sequence));
            miningSessionRepository.save(miningSession);
            sequence++;
            return true;
        }
    }
}
