package by.it.academy;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BlockDao;
import by.it.academy.service.*;
import by.it.academy.util.BlockUtil;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class TransactionCheck {

    public static SessionFactory factory = null;
    public static int difficulty = 3;

    static BlockDao blockDao = new BlockDao();
    static BlockService blockService = new BlockService();
    static TransactionService transactionService = new TransactionService();
    static UserService userService = new UserService();
    static WalletService walletService = new WalletService();
    static BlockchainUtxoService blockchainUtxoService = new BlockchainUtxoService();

    public static void main(String[] args) {
        factory = start();

        ArrayList<Block> blockchain = (ArrayList<Block>) blockDao.findAll(factory, "");
        Block block1 = BlockUtil.createBlock(blockchain.get(blockchain.size() - 1).hash);

        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions(factory);
        ArrayList<Transaction> transactionsToAdd = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getBlock() == null) {
                transactionsToAdd.add(transaction);
            }
        }

        for (Transaction transaction : transactionsToAdd) {
            blockService.addTransaction(block1, transaction);
            transaction.setBlock(block1);
            System.out.println(transaction);
            blockDao.create(factory, block1);
        }

        System.out.println("transactions in the block: " + block1.getTransactions());
        BlockUtil.mineBlock(block1, difficulty);
        blockDao.create(factory, block1);

        User anastasiya = null;
        User philipp = null;
        ArrayList<User> all = (ArrayList<User>) userService.findAll(factory);
        for (User user : all) {
            if
            (user.getUserName().equals("Anastasiya")) {
                anastasiya = user;
            }
            if
            (user.getUserName().equals("Philipp")) {
                philipp = user;
            }
        }

        ArrayList<Wallet> anastasiyaW = (ArrayList<Wallet>) walletService.getAll(factory, anastasiya.getUserId());
        System.out.println(walletService.getBalance(factory, anastasiyaW.get(0)));
        ArrayList<Wallet> philippW = (ArrayList<Wallet>) walletService.getAll(factory, philipp.getUserId());
        System.out.println(walletService.getBalance(factory, philippW.get(0)));

        finish(factory);
    }

    private static SessionFactory start() {
        StandardServiceRegistry registry = startRegistry();
        SessionFactory factory = null;

        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return factory;
    }

    private static StandardServiceRegistry startRegistry() {
        return new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
    }

    private static void finish(SessionFactory factory) {
        if (!factory.isClosed()) {
            factory.close();
            factory = null;
        }
    }
}
