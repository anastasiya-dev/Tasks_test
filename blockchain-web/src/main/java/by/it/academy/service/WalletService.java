package by.it.academy.service;

import by.it.academy.pojo.Wallet;
import by.it.academy.repository.WalletRepository;
import by.it.academy.support.WalletStatus;
import by.it.academy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class WalletService {

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    Wallet wallet;

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    public boolean saveWallet(Wallet wallet) {
        log.info("Saving wallet: " + wallet);
        if (wallet.getWalletId() != null && walletRepository.findById(wallet.getWalletId()).isPresent()) {
            log.warn("Denied. Already exists");
            return false;
        }
        log.info("Accepted");
        walletRepository.save(wallet);
        return true;
    }

    public List<Wallet> getAllWalletsForUser(String userId, WalletStatus walletStatus) {
        List<Wallet> all = walletRepository.findAll();
        List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : all) {
            if (wallet.getUserId().equals(userId) && wallet.getWalletStatus().equals(walletStatus)) {
                wallets.add(wallet);
            }
        }
        log.info("Extracting all the wallets for the user: " + userId + " with the status " + walletStatus);
        return wallets;
    }

    public Wallet findWalletById(String id) {
        log.info("Extracting wallet from repository - by id: " + id);
        return walletRepository.findById(id).get();
    }


    public Wallet createWallet(String userId) {
        wallet.setWalletId(userId + new Date().getTime());
        wallet.setUserId(userId);
        wallet.setWalletStatus(WalletStatus.ACTIVE);
        generateKeyPair(wallet);
        log.info("New wallet created: " + wallet);
        return wallet;
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
            log.info("New key pair for the wallet generated: " + wallet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Wallet delete(String walletId) {
        Wallet walletSaved = walletRepository.findById(walletId).get();
        walletSaved.setWalletStatus(WalletStatus.DELETED);
        walletRepository.save(walletSaved);
        log.info("Wallet deleted: " + walletId);
        return walletRepository.findById(walletId).get();
    }
}