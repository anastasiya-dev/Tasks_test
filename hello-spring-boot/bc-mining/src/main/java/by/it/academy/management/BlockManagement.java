package by.it.academy.management;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.*;
import by.it.academy.support.TransactionStatus;
import by.it.academy.support.WalletStatus;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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

    float minerReward = 0.05f;
    float senderReward = 0.01f;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(TransactionManagement.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(Block blockToAdd, Transaction transaction) {
        logger.info("Adding transactions to block " + blockToAdd.getBlockId());
        transaction.setBlockId(blockToAdd.getBlockId());
        transactionService.updateTransaction(blockToAdd, transaction);
        logger.info(transaction.getTransactionId());
    }

    public void mineBlock(Block block, int difficulty) {
        ArrayList<Transaction> blockTransactions = findTransactionsForBlock(block);
        block.setMerkleRoot(StringUtil.getMerkleRoot(blockTransactions));
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"

        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(blockService.calculateHash(block));
        }
        logger.info("Block Mined: " + block);
        String minerId = sendRewards(blockTransactions);
        block.setMinerId(minerId);
        blockService.saveBlock(block);
    }

    private ArrayList<Transaction> findTransactionsForBlock(Block block) {
        logger.info("Making transaction package for block " + block.getBlockId());
        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        ArrayList<Transaction> blockTransactions = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getBlockId() != null && transaction.getBlockId().equals(block.getBlockId())) {
                blockTransactions.add(transaction);
                logger.info(transaction.getTransactionId());
            }
        }
        return blockTransactions;
    }

    private String sendRewards(ArrayList<Transaction> blockTransactions) {
        logger.info("Sending rewards");
        ArrayList<Wallet> allWallets = walletService.findAllWallets(WalletStatus.ACTIVE);
        Random rnd = new Random();
        int walletChosen = rnd.nextInt(allWallets.size());
        String minerId = allWallets.get(walletChosen).getWalletId();

        for (Transaction transaction : blockTransactions) {
            transaction.setTransactionStatus(TransactionStatus.MINED);
            transactionService.updateTransaction(transaction);
            if (!walletService.findWalletById(transaction.getSenderId()).getWalletStatus().equals(WalletStatus.DELETED)) {
                makeRewardTransaction(transaction.getSenderId(), transaction, senderReward);
                logger.info("Ordinary reward: " + transaction);
            } else {
                makeRewardTransaction(
                        walletService.getAllWalletsForUser(
                                userService.findUserByName("Genesis1").getUserId(), WalletStatus.ACTIVE)
                                .get(0).getWalletId(), transaction, senderReward);
                logger.info("Genesis reward for deleted: " + transaction);
            }
            makeRewardTransaction(minerId, transaction, minerReward);
        }
        return minerId;
    }


    public void makeRewardTransaction(String walletId, Transaction transaction, float rate) {
        logger.info("Technical calculation of reward with the inputs:" +
                " walletId, transactionId, rate: " + walletId + " "
                + transaction.getTransactionId() + " " + rate);

        Wallet genesis = walletService.findAllWallets(WalletStatus.ACTIVE).stream().filter(
                w -> userService.findUserById(w.getUserId()).getUserName().equals("Genesis0")
        ).findFirst().get();
        String description = "reward_" + (rate * 100) + "%";
        Transaction transactionReward = transactionService.createTransaction(genesis.getWalletId(), walletId, (float) Math.round(transaction.getValue() * rate * 10.0f) / 10.0f);
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
    }
}
