package by.it.academy.service;

import by.it.academy.pojo.Wallet;
import by.it.academy.repository.WalletRepository;
import by.it.academy.util.StringUtil;
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

    public boolean saveWallet(Wallet wallet) {
        if (wallet.getWalletId() != null && walletRepository.findById(wallet.getWalletId()).isPresent()) {
            return false;
        }
        walletRepository.save(wallet);
        return true;
    }

    public List<Wallet> getAllWalletsForUser(String userId) {
        List<Wallet> all = walletRepository.findAll();
        List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : all) {
            if (wallet.getUserId().equals(userId)) {
                wallets.add(wallet);
            }
        }
        return wallets;
    }

    public Wallet findWalletById(String id) {
        return walletRepository.findById(id).get();
    }


    public Wallet createWallet(String userId) {
        wallet.setWalletId(userId + new Date().getTime());
        wallet.setUserId(userId);
        generateKeyPair(wallet);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}