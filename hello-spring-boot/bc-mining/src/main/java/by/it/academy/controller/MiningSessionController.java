package by.it.academy.controller;

import by.it.academy.BlockGenerator;
import by.it.academy.Consistency;
import by.it.academy.Genesis;
import by.it.academy.pojo.MiningSession;
import by.it.academy.service.MiningSessionService;
import by.it.academy.util.LoggerUtil;
import lombok.SneakyThrows;
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

    int difficulty = 3;

    @Autowired
    MiningSessionService miningSessionService;
    @Autowired
    Genesis genesis;
    @Autowired
    BlockGenerator blockGenerator;
    @Autowired
    Consistency consistency;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockController.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @PostMapping("/new-session")
    public ResponseEntity startMiningSession(@RequestBody MiningSession miningSession) throws IOException {

        logger.info("Received inquiry for mining session for wallet id: " + miningSession.getWalletId());
        boolean result = miningSessionService.createNewMiningSession(miningSession);

        if (result) {
            genesis.genesis(difficulty);
            blockGenerator.generateBlockchain(difficulty);
            consistency.isChainValid(difficulty);
            return new ResponseEntity(miningSession, HttpStatus.OK);
        } else {
            return new ResponseEntity(miningSession, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
