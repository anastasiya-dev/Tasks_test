package by.it.academy;

import by.it.academy.management.BlockManagement;
import by.it.academy.pojo.*;
import by.it.academy.service.*;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Component
public class Genesis {

    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;
    @Autowired
    UtxoService utxoService;
    @Autowired
    BlockManagement blockManagement;
    @Autowired
    MiningSessionService miningSessionService;
    @Autowired
    Consistency consistency;
    @Autowired
    BlockTemporaryService blockTemporaryService;
    @Autowired
    TransactionPackageService transactionPackageService;

    DateTimeFormatter formatter = ApplicationConfiguration.FORMATTER;
    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(Genesis.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void genesis(int difficulty) throws IOException {

        if (!userService.findUserByNameExistence("Genesis0") &&
                !userService.findUserByNameExistence("Genesis1")) {
            logger.info("Starting genesis");

            User genesisUser = userService.createAndSaveUser("Genesis0", "000");
            userService.appRoleAssignment(genesisUser.getUserId(), "ADMIN");
            Wallet genesisWallet = walletService.createAndSaveWallet(genesisUser.getUserId());
            logger.info("Genesis0 user: " + genesisUser);
            logger.info("Genesis0 user wallet: " + genesisWallet);

            User firstActualUser = userService.createAndSaveUser("Genesis1", "111");
            userService.appRoleAssignment(firstActualUser.getUserId(), "USER");
            Wallet firstActualWallet = walletService.createAndSaveWallet(firstActualUser.getUserId());
            logger.info("Genesis1 user: " + firstActualUser);
            logger.info("Genesis1 user wallet: " + firstActualWallet);

            Transaction genesisTransaction = transactionService.createTransaction(
                    genesisWallet.getWalletId(),
                    firstActualWallet.getWalletId(),
                    1000f
            );
            transactionService.generateSignature(genesisTransaction, genesisWallet.getPrivateKey());
            transactionService.saveTransaction(genesisTransaction);
            logger.info("Genesis transaction: " + genesisTransaction);

            Utxo utxo = utxoService.createUtxo(genesisTransaction.getTransactionId());
            utxo.setWalletId(firstActualWallet.getWalletId());
            utxo.setInputTransactionId("Genesis");
            utxoService.saveUtxo(utxo);
            logger.info("Genesis UTXO: " + utxo);

            MiningSession genesisMiningSession = miningSessionService.createMiningSession();
            genesisMiningSession.setSessionStart(LocalDateTime.now().format(formatter));
            genesisMiningSession.setWalletId(genesisWallet.getWalletId());
            genesisMiningSession.setBlockIdAttempted("0");
            miningSessionService.saveMiningSession(genesisMiningSession);
            logger.info("Genesis mining session created: " + genesisMiningSession);

            BlockTemporary genesisBlockTemporary = blockTemporaryService.createBlockTemporary(genesisMiningSession.getMiningSessionId());
            transactionPackageService.createTransactionPackage(
                    genesisMiningSession.getBlockIdAttempted(),
                    genesisTransaction.getTransactionId(),
                    genesisMiningSession.getMiningSessionId());
            logger.info("Genesis block temporary filled with transactions");

            MiningSession miningSessionProcessed = blockManagement.mineBlock(genesisBlockTemporary, difficulty, genesisMiningSession);
            logger.info("Genesis block mined: " + miningSessionProcessed.getBlockIdAttempted());
            logger.info("Genesis confirmation");
            logger.info(String.valueOf(consistency.isChainValid(ApplicationConfiguration.DIFFICULTY)));
            miningSessionProcessed.setConsistencyConfirmation(LocalDateTime.now().format(formatter));
            miningSessionService.updateSession(miningSessionProcessed);
        }
    }
}
