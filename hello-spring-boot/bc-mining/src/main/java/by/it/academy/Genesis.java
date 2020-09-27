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
import java.util.ArrayList;
import java.util.logging.Logger;

@Component
public class Genesis {

    @Autowired
    BlockService blockService;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void genesis(int difficulty) throws IOException {

        Logger logger = LoggerUtil.startLogging(Genesis.class.getName());

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
            logger.info("Genesis mining session created: " + genesisMiningSession);
            Block genesisBlock = blockService.createBlock("0", genesisMiningSession.getMiningSessionId());

            genesisMiningSession.setWalletId(genesisWallet.getWalletId());
            miningSessionService.saveMiningSession(genesisMiningSession);
            logger.info("Genesis block created: " + genesisBlock);


            BlockTemporary genesisBlockTemporary = blockTemporaryService.createBlockTemporary("0", genesisMiningSession.getMiningSessionId());
            blockManagement.addTransaction(genesisBlockTemporary, genesisTransaction);
            logger.info("Genesis block filled with transactions: " + genesisBlock);
            ArrayList<Block> blockchain = new ArrayList<>();

            blockManagement.mineBlock(genesisBlockTemporary, difficulty, genesisMiningSession, blockchain);
            logger.info("Genesis block mined: " + genesisBlock);
            logger.info("Genesis confirmation");
            System.out.println(consistency.isChainValid(ApplicationConfiguration.difficulty));
            ;
            genesisMiningSession.setConsistencyConfirmation(LocalDateTime.now().format(formatter));
            miningSessionService.updateSession(genesisMiningSession);
        }
    }
}
