package by.it.academy.controller;

import by.it.academy.pojo.MiningSession;
import by.it.academy.service.MiningSessionService;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class MiningSessionController {

    @Autowired
    MiningSessionService miningSessionService;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(MiningSessionController.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/new-session")
    public ResponseEntity<MiningSession> startMiningSession(@RequestBody MiningSession miningSession) {

        logger.info("Received inquiry for mining session for wallet id: " + miningSession.getWalletId());
        boolean result = miningSessionService.saveMiningSession(miningSession);
        return (result ?
                new ResponseEntity<>(miningSession, HttpStatus.OK) :
                new ResponseEntity<>(miningSession, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
