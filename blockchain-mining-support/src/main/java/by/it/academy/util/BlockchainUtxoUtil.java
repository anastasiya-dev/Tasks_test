package by.it.academy.util;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Wallet;

public class BlockchainUtxoUtil {

    //Check if coin belongs to you
    public static boolean isMine(BlockchainUtxo blockchainUtxo, Wallet wallet) {
        if (blockchainUtxo.getWallet() == null) {
            return false;
        } else {
            return (wallet.getWalletId().equals(blockchainUtxo.getWallet().getWalletId()));
        }
    }
}
