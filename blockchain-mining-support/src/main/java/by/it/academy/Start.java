package by.it.academy;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BlockDao;
import by.it.academy.service.*;
import by.it.academy.util.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Start {
    public static int difficulty = 3;
    public static SessionFactory factory = null;
    public static Transaction genesisTransaction;

    static BlockDao blockDao = new BlockDao();
    static BlockService blockService = new BlockService();
    static TransactionService transactionService = new TransactionService();
    static UserService userService = new UserService();
    static WalletService walletService = new WalletService();
    static BlockchainUtxoService blockchainUtxoService = new BlockchainUtxoService();

    public static void main(String[] args) {

        factory = start();

        User genesisUser = new User();
        genesisUser.setUserName("Genesis User");
        genesisUser.setUserPassword("000");
        userService.createNewUser(factory, genesisUser);

        Wallet genesisWallet = WalletUtil.createWallet();
        genesisWallet.setUser(genesisUser);
        genesisUser.getWallets().add(genesisWallet);
        userService.update(factory, genesisUser);

        Wallet firstActualWallet = walletService.findWalletById(factory, "4054c7bf-347f-480e-b86f-69a0b3011319");
        genesisTransaction = TransactionUtil.createTransaction(
                genesisWallet.getPublicKey(),
                firstActualWallet.getPublicKey(),
                1000f,
                null
        );
        TransactionUtil.generateSignature(genesisTransaction, genesisWallet.getPrivateKey());
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(TransactionOutputUtil.createTransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction)); //manually add the Transactions Output
        transactionService.createNewTransaction(factory, genesisTransaction);
        //см.закомменченную строку в конструкторе ютхо
        blockchainUtxoService.createNewUTXO(factory,
                BlockchainUtxoUtil.createBcUtxo(
                        genesisTransaction.getOutputs().get(0).getId(),
                        TransactionOutputUtil.createTransactionOutput(
                                genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction
                        )
                )
        );

        System.out.println("Creating and Mining Genesis block... ");

        Block genesisBlock = BlockUtil.createBlock("0");
        blockService.addTransaction(genesisBlock,genesisTransaction);
        genesisTransaction.getBlocks().add(genesisBlock);
        BlockUtil.mineBlock(genesisBlock, difficulty);
        blockDao.create(factory, genesisBlock);

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
