package by.it.academy.management;

import by.it.academy.Consistency;
import by.it.academy.pojo.*;
import by.it.academy.service.*;
import by.it.academy.support.MiningSessionStatus;
import by.it.academy.support.TransactionStatus;
import by.it.academy.support.WalletStatus;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    Consistency consistency;
    @Autowired
    TransactionPackageService transactionPackageService;
    @Autowired
    BlockTemporaryService blockTemporaryService;

    float minerReward = 0.05f;
    float senderReward = 0.01f;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockManagement.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(BlockTemporary blockToAdd, Transaction transaction) {
        transactionPackageService.createTransactionPackage(blockToAdd.getBlockId(), transaction.getTransactionId());
        logger.info("Adding transactions to block " + blockToAdd.getBlockId());
//        transaction.setBlockId(blockToAdd.getBlockId());
//        transactionService.updateTransaction(blockToAdd, transaction);
//        logger.info(transaction.getTransactionId());
    }

    public void mineBlock(BlockTemporary blockTemporary, int difficulty, MiningSession miningSession,
                          ArrayList<Block> blockchain) {
        ArrayList<Transaction> blockTransactions = findTransactionsForBlock(blockTemporary);
        blockTemporary.setMerkleRoot(StringUtil.getMerkleRoot(blockTransactions));
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"

        while (!blockTemporary.getHash().substring(0, difficulty).equals(target)) {
            blockTemporary.setNonce(blockTemporary.getNonce() + 1);
            blockTemporary.setHash(blockTemporaryService.calculateHash(blockTemporary));
            if (blockTemporary.getNonce() % 50_000 == 0) {
                if (checkIfCurrentBlockAlreadyMined(blockchain, blockTemporary)) {
                    logger.info("Breaking nonce search as block mined by another thread");
                    break;
                }
            }
        }
        logger.info("Block Mined: " + blockTemporary);
        blockTemporaryService.updateBlockTemporary(blockTemporary);

        ArrayList<Block> currentBlockchain = blockService.findAllBlocks();
        currentBlockchain.sort(Comparator.comparingLong(Block::getTimeStamp));
        if (currentBlockchain.size() == 0 || currentBlockchain.get(currentBlockchain.size() - 1).getHash().equals(blockTemporary.getPreviousHash())) {
            logger.info("Setting blockId for transactions");
            Block block = blockService.transformFromTemporary(blockTemporary);

            for (Transaction transaction : blockTransactions) {
                System.out.println("Adding actual block Id in system: " + transaction.getTransactionId());
                addTransaction(blockTemporary, transaction);
            }
            blockAndSessionSaving(block, miningSession);
        } else {
            miningSessionDiscard(miningSession);
        }
    }

    private void miningSessionDiscard(MiningSession miningSession) {
        logger.info("Mining session discard: " + miningSession.getMiningSessionId());
        miningSession.setBlockId("none");
        miningSession.setMiningSessionStatus(MiningSessionStatus.FAILURE);
        miningSession.setMinerReward(0.0f);
        miningSession.setSessionEnd(LocalDateTime.now().format(formatter));
        miningSessionService.updateSession(miningSession);
    }

    private void blockAndSessionSaving(Block block, MiningSession miningSession) {

        logger.info("Mining session success: " + miningSession.getMiningSessionId());
        float minerRewardTotalSum = sendRewards(block.getBlockId(), miningSession);
        block.setMiningSessionId(miningSession.getMiningSessionId());
        block.setMinerId(miningSession.getWalletId());
        blockService.saveBlock(block);

        miningSession.setBlockId(block.getBlockId());
        miningSession.setMiningSessionStatus(MiningSessionStatus.SUCCESS);
        miningSession.setMinerReward(minerRewardTotalSum);
        miningSession.setSessionEnd(LocalDateTime.now().format(formatter));
        miningSessionService.updateSession(miningSession);
    }

    private boolean checkIfCurrentBlockAlreadyMined(ArrayList<Block> blockchain, BlockTemporary blockTemporary) {
        logger.info("Check if current block already mined");
        if (blockTemporary.getBlockId().equals("0_genesis") || blockService.findAllBlocks().size() == blockchain.size()) {
            logger.info("No");
            return false;
        } else {
            logger.info("Yes");
            return true;
        }
    }

    private ArrayList<Transaction> findTransactionsForBlock(BlockTemporary blockTemporary) {
        logger.info("Making transaction package for block " + blockTemporary.getBlockId());

        ArrayList<TransactionPackage> transactionPackages = transactionPackageService.findAllTransactionPackagesByBlockId(blockTemporary.getBlockId());

        ArrayList<Transaction> blockTransactions = new ArrayList<>();
        for (TransactionPackage transactionPackage : transactionPackages) {
            if (transactionPackage.getBlockId() != null && transactionPackage.getBlockId().equals(blockTemporary.getBlockId())) {
                blockTransactions.add(transactionService.findTransactionById(transactionPackage.getTransactionId()));
                logger.info(transactionPackage.getTransactionId());
            }
        }
        return blockTransactions;
    }

    private float sendRewards(String blockId, MiningSession miningSession) {
        logger.info("Sending rewards");
        float minerRewardTotalSum = 0.0f;
        String genesisWalletId = walletService.getAllWalletsForUser(
                userService.findUserByName("Genesis1").getUserId(), WalletStatus.ACTIVE)
                .get(0).getWalletId();

        ArrayList<Transaction> blockTransactions = findTransactionsForBlock(blockTemporaryService.findBlockTemporaryById(blockId));

        for (Transaction transactionInPackage : blockTransactions) {
            transactionInPackage.setTransactionStatus(TransactionStatus.MINED);
            transactionService.updateTransaction(transactionInPackage);


            transactionService.findTransactionById(transactionInPackage.getTransactionId()).setBlockId(blockId);
            transactionService.updateTransaction(blockService.findBlockById(blockId), transactionInPackage);

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
}
