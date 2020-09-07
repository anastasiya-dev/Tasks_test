package by.it.academy.util;

import by.it.academy.pojo.Wallet;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.stereotype.Service;

import java.security.*;
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
            wallet.publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
//    public static float getBalance(Wallet wallet) {
//        float total = 0;
//        for (Map.Entry<String, TransactionOutput> item : BlockChain.UTXOs.entrySet()) {
//            TransactionOutput UTXO = item.getValue();
//            if (TrOutputService.isMine(UTXO, wallet.publicKey)) { //if output belongs to me ( if coins belong to me )
//                wallet.UTXOs.put(UTXO.id, UTXO); //add it to our list of unspent transactions.
//                total += UTXO.value;
//            }
//        }
//        return total;
//    }

//    //Generates and returns a new transaction from this wallet.
//    public static Transaction sendFunds(Wallet wallet, PublicKey _recipient, float value) {
//        if (getBalance(wallet) < value) { //gather balance and check funds.
//            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
//            return null;
//        }
//        //create array list of inputs
//        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
//
//        float total = 0;
//        for (Map.Entry<String, TransactionOutput> item : wallet.UTXOs.entrySet()) {
//            TransactionOutput UTXO = item.getValue();
//            total += UTXO.value;
//            inputs.add(TrInputService.createTransactionInput(UTXO.id));
//            if (total > value) break;
//        }
//
//        Transaction newTransaction = new Transaction(wallet.publicKey, _recipient, value, inputs);
//        newTransaction.generateSignature(wallet.privateKey);
//
//        for (TransactionInput input : inputs) {
//            wallet.UTXOs.remove(input.transactionOutputId);
//        }
//        return newTransaction;
//    }
}
