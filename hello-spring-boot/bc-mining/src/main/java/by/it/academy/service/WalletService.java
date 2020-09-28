package by.it.academy.service;

import by.it.academy.pojo.Wallet;
import by.it.academy.repository.WalletRepository;
import by.it.academy.support.WalletStatus;
import by.it.academy.util.LoggerUtil;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
//@Transactional
public class WalletService {

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    Wallet wallet;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(WalletService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Wallet> getAllWalletsForUser(String userId, WalletStatus walletStatus) {
        logger.info("Extracting all the wallets for the user: " + userId + " with the status " + walletStatus);
        List<Wallet> allWallets = (List<Wallet>) walletRepository.findAll();
        List<Wallet> userWallets = new ArrayList<>();
        for (Wallet wallet : allWallets) {
            if (wallet.getUserId().equals(userId) && wallet.getWalletStatus().equals(WalletStatus.ACTIVE)) {
                userWallets.add(wallet);
            }
        }
        return userWallets;
    }

    public Wallet findWalletById(String id) {
        logger.info("Extracting wallet from repository - by id: " + id);
        return walletRepository.findById(id).orElse(null);
    }

    public Wallet createAndSaveWallet(String userId) {
        wallet.setWalletId(userId + new Date().getTime());
        wallet.setUserId(userId);
        generateKeyPair(wallet);
        wallet.setWalletStatus(WalletStatus.ACTIVE);
        logger.info("New wallet created: " + wallet);
        return walletRepository.save(wallet);
    }

    private void generateKeyPair(Wallet wallet) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            wallet.setPrivateKey(keyPair.getPrivate());
            wallet.setPrivateKeyString(StringUtil.getStringFromKey(wallet.getPrivateKey()));
            wallet.setPublicKey(keyPair.getPublic());
            wallet.setPublicKeyString(StringUtil.getStringFromKey(wallet.getPublicKey()));
            logger.info("New key pair for the wallet generated: " + wallet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Wallet> findAllWallets(WalletStatus walletStatus) {
        logger.info("Extracting all wallets with the status " + walletStatus);
        List<Wallet> allWallets = (List<Wallet>) walletRepository.findAll();
        ArrayList<Wallet> activeWallets = new ArrayList<>();
        for (Wallet wallet : allWallets) {
            if (wallet.getWalletStatus().equals(WalletStatus.ACTIVE)) {
                activeWallets.add(wallet);
            }
        }
        return activeWallets;
    }

}
