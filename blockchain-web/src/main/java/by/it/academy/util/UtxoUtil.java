package by.it.academy.util;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;

public class UtxoUtil {


    //Check if coin belongs to you
    public static boolean isMine(Utxo utxo, Wallet wallet) {
        if (utxo.getWallet() == null) {
            return false;
        } else {
            return (wallet.getWalletId().equals(utxo.getWallet().getWalletId()));
        }
    }
}
