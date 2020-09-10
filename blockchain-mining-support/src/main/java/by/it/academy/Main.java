package by.it.academy;

import by.it.academy.pojo.*;
import by.it.academy.repository.BlockDao;
import by.it.academy.repository.BlockchainUtxoDao;
import by.it.academy.service.BlockService;
import by.it.academy.service.TransactionService;
import by.it.academy.util.BlockUtil;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static int difficulty = 3;
    public static SessionFactory factory = null;

    public static void main(String[] args) {


        factory = start();

        BlockchainUtxoDao blockchainUtxoDao = new BlockchainUtxoDao();
        List<BlockchainUtxo> all = blockchainUtxoDao.findAll(factory, "");
        System.out.println(all);

        Block genesis = BlockUtil.createBlock("0");
        BlockDao blockDao = new BlockDao();
        BlockService blockService = new BlockService();
        TransactionService transactionService = new TransactionService();
        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        for (Transaction transaction : allTransactions) {
            blockService.addTransaction(genesis, transaction);
        }
        System.out.println("before saving");
        System.out.println(genesis);
        blockDao.create(factory, genesis);
        System.out.println("after saving");
        System.out.println(genesis);

        finish(factory);

    }

    private static SessionFactory start() {
        StandardServiceRegistry registry = startRegistry();
        SessionFactory factory = null;

        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
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
