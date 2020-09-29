package by.it.academy.management;

import by.it.academy.ApplicationConfiguration;
import by.it.academy.pojo.*;
import by.it.academy.service.*;
import by.it.academy.support.MiningSessionStatus;
import by.it.academy.support.TransactionStatus;
import by.it.academy.support.WalletStatus;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;

@Service
public class BlockManagement {

    @Autowired
    TransactionService transactionService;
    @Autowired
    BlockService blockService;
    @Autowired
    WalletService walletService;
    @Autowired
    UserService userService;
    @Autowired
    UtxoService utxoService;
    @Autowired
    MiningSessionService miningSessionService;
    @Autowired
    TransactionPackageService transactionPackageService;
    @Autowired
    BlockTemporaryService blockTemporaryService;

    float minerReward = ApplicationConfiguration.MINER_REWARD;
    float senderReward = ApplicationConfiguration.SENDER_REWARD;
    DateTimeFormatter formatter = ApplicationConfiguration.FORMATTER;
    int checkFlag = ApplicationConfiguration.CHECK_FLAG;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockManagement.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MiningSession mineBlock(BlockTemporary blockTemporary, int difficulty, MiningSession miningSession) {
        ArrayList<Transaction> blockTransactions = findTransactionsForBlock(miningSession.getMiningSessionId());
        blockTemporary.setMerkleRoot(StringUtil.getMerkleRoot(blockTransactions));
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"

        while (!blockTemporary.getHash().substring(0, difficulty).equals(target)) {
            blockTemporary.setNonce(blockTemporary.getNonce() + 1);
            blockTemporary.setHash(blockTemporaryService.calculateHash(blockTemporary));
            if (blockTemporary.getNonce() % checkFlag == 0) {
                logger.info("Nonce+block: " + blockTemporary.getNonce() + " + " + blockTemporary.getBlockTemporaryId());
                if (checkIfCurrentBlockAlreadyMined(miningSession.getBlockIdAttempted(), blockTemporary)) {
                    logger.info("Breaking nonce search as block mined by another thread");
                    break;
                }
            }
        }
        logger.info("Mining finished: " + blockTemporary);
        blockTemporaryService.updateBlockTemporary(blockTemporary);
        return resultingChanges(blockTemporary.getBlockTemporaryId(), miningSession.getMiningSessionId());
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    private MiningSession resultingChanges(String blockTemporaryId, String miningSessionId) {
        MiningSession miningSession = miningSessionService.findById(miningSessionId);
        BlockTemporary blockTemporary = blockTemporaryService.findBlockTemporaryById(blockTemporaryId);

        if (successCheck(blockTemporaryService.findBlockTemporaryById(blockTemporary.getBlockTemporaryId()))) {
            logger.info("Setting blockId on transactions for successful session (blockId): " + blockTemporary.getBlockId());
            blockTemporary.setBlockId(miningSession.getBlockIdAttempted());
            blockTemporaryService.updateBlockTemporary(blockTemporary);
            blockService.transformFromTemporary(blockTemporary);
            return miningSuccessHandler(miningSession.getMiningSessionId());
        } else {
            return miningFailureHandler(miningSession.getMiningSessionId());
        }
    }

    private boolean successCheck(BlockTemporary blockTempChecked) {
        logger.info("Success check for block temporary: " + blockTempChecked.getBlockId());
        ArrayList<Block> currentBlockchain = blockService.findAllBlocks();
        currentBlockchain.sort(Comparator.comparingLong(Block::getTimeStamp));
        if (currentBlockchain.isEmpty() || currentBlockchain.get(currentBlockchain.size() - 1).getHash().equals(blockTempChecked.getPreviousHash())) {
            logger.info("Success");
            return true;
        } else {
            logger.info("Failure");
            return false;
        }
    }

    private MiningSession miningFailureHandler(String miningSessionId) {
        MiningSession miningSession = miningSessionService.findById(miningSessionId);
        logger.info("Mining session discard: " + miningSessionId);
        miningSession.setBlockIdActual("none");
        miningSession.setMiningSessionStatus(MiningSessionStatus.FAILURE);
        miningSession.setMinerReward(0.0f);
        miningSession.setSessionEnd(LocalDateTime.now().format(formatter));
        miningSessionService.updateSession(miningSession);
        return miningSession;
    }

    private MiningSession miningSuccessHandler(String miningSessionId) {
        MiningSession miningSession = miningSessionService.findById(miningSessionId);
        Block block = blockService.findBlockById(miningSession.getBlockIdAttempted());
        logger.info("Mining session success: " + miningSession.getMiningSessionId());
        float minerRewardTotalSum = sendRewards(miningSession);
        block.setMiningSessionId(miningSession.getMiningSessionId());
        block.setMinerId(miningSession.getWalletId());
        blockService.saveBlock(block);

        miningSession.setBlockIdActual(block.getBlockId());
        miningSession.setMiningSessionStatus(MiningSessionStatus.SUCCESS);
        miningSession.setMinerReward(minerRewardTotalSum);
        miningSession.setSessionEnd(LocalDateTime.now().format(formatter));
        miningSessionService.updateSession(miningSession);
        return miningSession;
    }

    private boolean checkIfCurrentBlockAlreadyMined(String blockIdAttempted, BlockTemporary blockTemporary) {
        logger.info("Check if current block already mined");
        if (blockTemporary.getBlockTemporaryId().equals("0_0") || String.valueOf(blockService.findAllBlocks().size()).equals(blockIdAttempted)) {
            logger.info("No");
            return false;
        } else {
            logger.info("Yes");
            return true;
        }
    }


    private float sendRewards(MiningSession miningSession) {
        logger.info("Received mining session in send: " + miningSession);
        String blockIdAttempted = miningSession.getBlockIdAttempted();
        logger.info("Received block id attempted to find block and send rewards: " + blockIdAttempted);
        logger.info("Sending rewards");
        float minerRewardTotalSum = 0.0f;
        String genesisWalletId = walletService.getAllWalletsForUser(
                userService.findUserByName("Genesis1").getUserId(), WalletStatus.ACTIVE)
                .get(0).getWalletId();

        ArrayList<Transaction> blockTransactions = findTransactionsForBlock(miningSession.getMiningSessionId());

        for (Transaction transactionInPackage : blockTransactions) {
            transactionInPackage.setTransactionStatus(TransactionStatus.MINED);
            transactionService.updateTransaction(transactionInPackage);
            transactionService.findTransactionById(transactionInPackage.getTransactionId()).setBlockId(blockIdAttempted);
            transactionService.updateTransaction(blockService.findBlockById(blockIdAttempted), transactionInPackage);

            if (!walletService.findWalletById(transactionInPackage.getSenderId()).getWalletStatus().equals(WalletStatus.DELETED)) {
                makeRewardTransaction(transactionInPackage.getSenderId(), transactionInPackage, senderReward);
                logger.info("Ordinary sender reward: " + transactionInPackage.getTransactionId());
            } else {
                makeRewardTransaction(genesisWalletId, transactionInPackage, senderReward);
                logger.info("Genesis sender reward for deleted: " + transactionInPackage.getTransactionId());
            }

            float minerRewardItem = 0.0f;
            if (!walletService.findWalletById(miningSession.getWalletId()).getWalletStatus().equals(WalletStatus.DELETED)) {
                minerRewardItem = makeRewardTransaction(miningSession.getWalletId(), transactionInPackage, minerReward);
                logger.info("Ordinary miner reward: " + transactionInPackage.getTransactionId());
            } else {
                minerRewardItem = makeRewardTransaction(genesisWalletId, transactionInPackage, minerReward);
                logger.info("Genesis miner reward for deleted: " + transactionInPackage.getTransactionId());
            }
            minerRewardTotalSum += minerRewardItem;
        }
        return minerRewardTotalSum;
    }


    public float makeRewardTransaction(String walletId, Transaction transactionMined, float rate) {
        logger.info("Technical calculation of reward with the inputs:" +
                " walletId, transactionId, rate: " + walletId + " "
                + transactionMined.getTransactionId() + " " + rate);

        Wallet genesis = walletService.findAllWallets(WalletStatus.ACTIVE).stream().filter(
                w -> userService.findUserById(w.getUserId()).getUserName().equals("Genesis0")
        ).findFirst().get();
        String description = "reward_" + (rate * 100) + "%";
        Transaction transactionReward = transactionService.createTransaction(genesis.getWalletId(), walletId, (float) Math.round(transactionMined.getValue() * rate * 10.0f) / 10.0f);

        transactionService.generateSignature(transactionReward, genesis.getPrivateKey());
        transactionReward.setBlockId(description);
        if (rate == minerReward) {
            transactionReward.setTransactionStatus(TransactionStatus.REWARD_M);
        }
        if (rate == senderReward) {
            transactionReward.setTransactionStatus(TransactionStatus.REWARD_S);
        }
        transactionService.saveTransaction(transactionReward);
        Utxo utxo = utxoService.createUtxo(transactionReward.getTransactionId());
        utxo.setWalletId(walletId);
        utxo.setInputTransactionId(description);
        utxoService.saveUtxo(utxo);
        return transactionReward.getValue();
    }

    private ArrayList<Transaction> findTransactionsForBlock(String miningSessionId) {
        MiningSession miningSession = miningSessionService.findById(miningSessionId);
        logger.info("Making transaction package for session " + miningSessionId);

        ArrayList<TransactionPackage> transactionPackages = transactionPackageService.findAllTransactionPackagesByBlockId(miningSession.getBlockIdAttempted());

        ArrayList<Transaction> blockTransactions = new ArrayList<>();
        for (TransactionPackage transactionPackage : transactionPackages) {
            if (transactionPackage.getBlockAttemptedId() != null
                    && transactionPackage.getBlockAttemptedId().equals(miningSession.getBlockIdAttempted())
                    && transactionPackage.getMiningSessionId().equals(miningSessionId)) {
                blockTransactions.add(transactionService.findTransactionById(transactionPackage.getTransactionId()));
                logger.info(transactionPackage.getTransactionId());
            }
        }
        return blockTransactions;
    }
}
