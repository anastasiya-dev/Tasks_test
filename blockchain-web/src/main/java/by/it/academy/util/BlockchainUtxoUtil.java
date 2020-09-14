package by.it.academy.util;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.service.TransactionService;

import java.security.PublicKey;

public class BlockchainUtxoUtil {


    //Check if coin belongs to you
    public static boolean isMine(BlockchainUtxo blockchainUtxo, PublicKey publicKey) {
        return (publicKey.equals(blockchainUtxo.getRecipient()));
    }
}
