package by.it.academy.service;

import by.it.academy.controller.BlockController;
import by.it.academy.pojo.MiningSession;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.MiningSessionRepository;
import by.it.academy.support.MiningSessionStatus;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class MiningSessionService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    @Autowired
    MiningSession miningSession;

    public boolean saveMiningSession(MiningSession miningSession) {
        logger.info("Processing mining session creation");
        try {
            miningSessionRepository.findById(miningSession.getMiningSessionId()).get();
            logger.warning("Denied. Already exists");
            return false;
        } catch (NoSuchElementException e) {

            ArrayList<MiningSession> miningSessions = findAllMiningSessions();
            if (miningSessions.isEmpty()) {
                miningSession.setMiningSessionId("0");
                miningSession.setSessionStart(LocalDateTime.now().format(formatter));
            } else {
                miningSession.setMiningSessionId(String.valueOf(miningSessions.size()));
            }
            logger.info("Accept");
            miningSessionRepository.save(miningSession);
            return true;
        }
    }


    public MiningSession createMiningSession () {
        miningSession.setMiningSessionId("genesis");
        return miningSession;
    }

    public ArrayList<MiningSession> findAllMiningSessions() {
        return (ArrayList<MiningSession>) miningSessionRepository.findAll();
    }

    public MiningSession updateSession(MiningSession miningSession) {
        logger.info("Updating mining session");
        String id = miningSession.getMiningSessionId();
        MiningSession savedMiningSession = miningSessionRepository.findById(id).orElseThrow();
        logger.info("Initial: " + savedMiningSession);
        logger.info("New: " + miningSession);
        if (savedMiningSession.equals(miningSession)) {
            return savedMiningSession;
        } else {
            savedMiningSession.setBlockId(miningSession.getBlockId());
            savedMiningSession.setMiningSessionStatus(miningSession.getMiningSessionStatus());
            savedMiningSession.setMinerReward(miningSession.getMinerReward());
            savedMiningSession.setSessionEnd(miningSession.getSessionEnd());
            miningSessionRepository.save(savedMiningSession);
        }
        return miningSessionRepository.findById(id).orElseThrow();
    }
}
