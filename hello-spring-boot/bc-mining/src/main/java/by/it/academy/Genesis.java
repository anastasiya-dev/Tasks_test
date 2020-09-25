package by.it.academy;

import by.it.academy.management.BlockManagement;
import by.it.academy.pojo.*;
import by.it.academy.service.*;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class Genesis {

    @Autowired
    Transaction genesisTransaction;
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

            genesisTransaction = transactionService.createTransaction(
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

            Block genesisBlock = blockService.createBlock("0");
            MiningSession genesisMiningSession = miningSessionService.createMiningSession();
            genesisMiningSession.setWalletId(genesisWallet.getWalletId());
            miningSessionService.saveMiningSession(genesisMiningSession);
            logger.info("Genesis block created: " + genesisBlock);
            blockManagement.addTransaction(genesisBlock, genesisTransaction);
            logger.info("Genesis block filled with transactions: " + genesisBlock);
            blockManagement.mineBlock(genesisBlock, difficulty, genesisMiningSession);
            logger.info("Genesis block mined: " + genesisBlock);
        }
    }
}
