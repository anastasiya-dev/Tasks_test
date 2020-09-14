package by.it.academy.util;

import by.it.academy.pojo.Utxo;

import java.security.PublicKey;

public class UtxoUtil {


    //Check if coin belongs to you
    public static boolean isMine(Utxo utxo, PublicKey publicKey) {
        return (publicKey.equals(utxo.getRecipient()));
    }
}
