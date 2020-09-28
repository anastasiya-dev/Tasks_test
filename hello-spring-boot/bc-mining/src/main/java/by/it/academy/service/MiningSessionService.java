package by.it.academy.service;

import by.it.academy.ApplicationConfiguration;
import by.it.academy.pojo.MiningSession;
import by.it.academy.repository.MiningSessionRepository;
import by.it.academy.support.MiningSessionStatus;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
//@Transactional
public class MiningSessionService {

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(MiningSessionService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    MiningSessionRepository miningSessionRepository;
    @Autowired
    MiningSession miningSession;

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
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
            } else {
                miningSession.setMiningSessionId(String.valueOf(miningSessions.size()));
            }
            miningSession.setSessionRequest(LocalDateTime.now().format(ApplicationConfiguration.FORMATTER));
            logger.info("Accept");
            miningSessionRepository.save(miningSession);
            return true;
        }
    }


    public MiningSession createMiningSession() {
        logger.info("Creating mining session");
        miningSession.setMiningSessionId("0");
        return miningSession;
    }

    public ArrayList<MiningSession> findAllMiningSessions() {
        logger.info("Extracting all mining sessions");
        return (ArrayList<MiningSession>) miningSessionRepository.findAll();
    }

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
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
            savedMiningSession.setConsistencyConfirmation(miningSession.getConsistencyConfirmation());
            savedMiningSession.setSessionStart(miningSession.getSessionStart());
            savedMiningSession.setConsistencyConfirmation(miningSession.getConsistencyConfirmation());
            miningSessionRepository.save(savedMiningSession);
        }
        return miningSessionRepository.findById(id).orElseThrow();
    }

    public ArrayList<MiningSession> findAllMiningSessionsByStatus(MiningSessionStatus miningSessionStatus) {
        logger.info("Extracting all mining sessions for particular status: " + miningSessionStatus);
        ArrayList<MiningSession> miningSessionsAll = findAllMiningSessions();
        ArrayList<MiningSession> miningSessionsByStatus = new ArrayList<>();
        for (MiningSession miningSession : miningSessionsAll) {
            if (miningSession.getMiningSessionStatus().equals(miningSessionStatus)) {
                miningSessionsByStatus.add(miningSession);
            }
        }
        return miningSessionsByStatus;
    }

    public MiningSession findById(String miningSessionId) {
        logger.info("Extracting mining session by id: " + miningSessionId);
        return miningSessionRepository.findById(miningSessionId).get();
    }
}
