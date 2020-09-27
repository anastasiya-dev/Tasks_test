package by.it.academy.pojo;

import by.it.academy.MiningAlgorithmService;
import by.it.academy.service.MiningSessionService;
import org.springframework.beans.factory.annotation.Autowired;

public class MiningAlgorithm implements Runnable {

    @Autowired
    MiningAlgorithmService miningAlgorithmService;
    @Autowired
    MiningSessionService miningSessionService;

    String miningSessionId;
//    String source;

    public MiningAlgorithm(String miningSessionId) {
        this.miningSessionId = miningSessionId;
    }

    public void run() {
        miningAlgorithmService.run(miningSessionService.findById(miningSessionId));
    }
}
