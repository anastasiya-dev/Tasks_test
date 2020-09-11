package by.it.academy;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionOutput;
import by.it.academy.repository.BlockDao;
import by.it.academy.service.BlockService;
import by.it.academy.service.TransactionService;
import by.it.academy.util.BlockUtil;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class Start {
    public static int difficulty = 3;
    public static SessionFactory factory = null;
    public static Transaction genesisTransaction;

    static BlockDao blockDao = new BlockDao();
    static BlockService blockService = new BlockService();
    static TransactionService transactionService = new TransactionService();

    public static void main(String[] args) {

        factory = start();
//
//        //create genesis transaction, which sends 100 NoobCoin to walletA:
//        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
//        genesisTransaction.generateSignature(coinbase.privateKey);     //manually sign the genesis transaction
//        genesisTransaction.transactionId = "0"; //manually set the transaction id
//        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
//        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        //genesis block
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = BlockUtil.createBlock("0");
        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        for (Transaction transaction : allTransactions) {
            blockService.addTransaction(genesis, transaction);
        }
        BlockUtil.mineBlock(genesis, difficulty);
        blockDao.create(factory, genesis);
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
