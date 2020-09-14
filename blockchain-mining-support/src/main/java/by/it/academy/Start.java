package by.it.academy;

import by.it.academy.pojo.*;
import by.it.academy.repository.BlockDao;
import by.it.academy.service.*;
import by.it.academy.util.BlockUtil;
import by.it.academy.util.TransactionUtil;
import by.it.academy.util.WalletUtil;
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

        User firstActualUser = null;
        ArrayList<User> all = (ArrayList<User>) userService.findAll(factory);
        for (User user : all) {
            if
            (user.getUserName().equals("Anastasiya")) {
                firstActualUser = user;
                break;
            }
        }
        String firstActualWalletId = null;
        ArrayList<Wallet> walletServiceAll = (ArrayList<Wallet>) walletService.getAll(factory, firstActualUser.getUserId());
        System.out.println(walletServiceAll);
        for (Wallet wallet : walletServiceAll) {
            if
            (wallet.getUser().getUserName().equals("Anastasiya")) {
                firstActualWalletId = wallet.getWalletId();
                break;
            }
        }
        System.out.println(firstActualWalletId);
        Wallet firstActualWallet = walletService.findWalletById(factory, firstActualWalletId);
        System.out.println(firstActualWallet);
        genesisTransaction = TransactionUtil.createTransaction(
                genesisWallet.getPublicKey(),
                firstActualWallet.getPublicKey(),
                1000f
        );
        TransactionUtil.generateSignature(genesisTransaction, genesisWallet.getPrivateKey());
//        genesisTransaction.transactionId = "0"; //manually set the transaction id
//        genesisTransaction.outputs.add(TransactionOutputUtil.createTransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction)); //manually add the Transactions Output
        transactionService.createNewTransaction(factory, genesisTransaction);
        //см.закомменченную строку в конструкторе ютхо
        BlockchainUtxo bcUtxo = blockchainUtxoService.createBcUtxo(factory, genesisTransaction.transactionId);
//        bcUtxo.setOutputTransactionId(genesisTransaction.transactionId);
        bcUtxo.setWallet(firstActualWallet);
        firstActualWallet.getUTXOs().add(bcUtxo);
//        blockchainUtxoService.createNewUTXO(factory, bcUtxo);
        walletService.createNewWallet(factory, firstActualWallet);

        System.out.println("Creating and Mining Genesis block... ");

        Block genesisBlock = BlockUtil.createBlock("0");
        genesisTransaction.setBlock(genesisBlock);
        blockService.addTransaction(factory, genesisBlock, genesisTransaction);
        blockService.mineBlock(factory, genesisBlock, difficulty);
//        blockDao.create(factory, genesisBlock);

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
