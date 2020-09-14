package by.it.academy;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BlockDao;
import by.it.academy.service.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class BalanceTest {

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
        System.out.println("Anastasiya cash:");
        for(Wallet wallet: anastasiyaW){
            for(BlockchainUtxo blockchainUtxo: wallet.getUTXOs()){
                System.out.println(blockchainUtxo.getBlockchainUtxoId());
            }
        }
        ArrayList<Wallet> philippW = (ArrayList<Wallet>) walletService.getAll(factory, philipp.getUserId());
        System.out.println(walletService.getBalance(factory, philippW.get(0)));
        System.out.println("Philipp cash:");
        for(Wallet wallet: philippW){
            for(BlockchainUtxo blockchainUtxo: wallet.getUTXOs()){
                System.out.println(blockchainUtxo.getBlockchainUtxoId());
            }
        }
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
