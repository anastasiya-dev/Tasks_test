package by.it.academy.management;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import org.springframework.stereotype.Service;

@Service
public class UtxoManagement {

    public boolean isMine(Utxo utxo, Wallet wallet) {
        if (utxo.getWalletId() == null) {
            return false;
        } else {
            return (wallet.getWalletId().equals(utxo.getWalletId()));
        }
    }
}
