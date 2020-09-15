package by.it.academy.util;

import by.it.academy.pojo.Wallet;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class WalletUtil {

    public static Wallet createWallet() {
        Wallet wallet = new Wallet();
        generateKeyPair(wallet);
        return wallet;
    }

    private static void generateKeyPair(Wallet wallet) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            wallet.privateKey = keyPair.getPrivate();
            wallet.privateKeyString = StringUtil.getStringFromKey(wallet.privateKey);
            wallet.publicKey = keyPair.getPublic();
            wallet.setWalletId(StringUtil.getStringFromKey(wallet.publicKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
