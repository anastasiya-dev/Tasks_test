package by.it.academy.service;

import by.it.academy.pojo.MiningSession;
import by.it.academy.repository.MiningSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@Transactional
public class MiningSessionService {

    private static final Logger logger = LoggerFactory.getLogger(MiningSessionService.class);

    @Autowired
    MiningSessionRepository miningSessionRepository;
    @Autowired
    WalletService walletService;

    public ArrayList<MiningSession> findAllMiningSessions() {
        logger.info("Extracting all mining sessions");
        return (ArrayList<MiningSession>) miningSessionRepository.findAll();
    }

    public ArrayList<MiningSession> findAllMiningSessionsForUser(String userId) {
        List<MiningSession> all = miningSessionRepository.findAll();
        System.out.println("all ms " + all);
        ArrayList<MiningSession> miningSessions = new ArrayList<>();
        for (MiningSession miningSession : all) {
            System.out.println("processing ms:" + miningSession);
            if (walletService.findWalletById(miningSession.getWalletId()).getUserId().equals(userId)) {
                miningSessions.add(miningSession);
            }
        }
        logger.info("Extracting all the mining sessions for the user: " + userId);
        return miningSessions;
    }
}
