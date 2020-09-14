package by.it.academy;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
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
    static UtxoService utxoService = new UtxoService();

    public static void main(String[] args) {
        factory = start();

        ArrayList<Block> blockchain = (ArrayList<Block>) blockDao.findAll(factory, "");
        long lastBlockTs = 0;
        String lastBlockHash = null;
        for (Block block : blockchain) {
            if (block.getTimeStamp() > lastBlockTs) {
                lastBlockTs = block.getTimeStamp();
                lastBlockHash = block.getHash();
            }
        }
        Block block1 = BlockUtil.createBlock(lastBlockHash);

        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions(factory);
        ArrayList<Transaction> transactionsToAdd = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getBlock() == null) {
                transactionsToAdd.add(transaction);
            }
        }

        for (Transaction transaction : transactionsToAdd) {
            transaction.setBlock(block1);
            System.out.println("in the transaction add loop");
            blockService.addTransaction(factory, block1, transaction);
        }

        System.out.println("transactions in the block: " + block1.getTransactions());
        System.out.println("Going to start mining");
        blockService.mineBlock(factory, block1, difficulty);

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
