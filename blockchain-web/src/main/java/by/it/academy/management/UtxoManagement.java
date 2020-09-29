package by.it.academy.management;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UtxoManagement {

    private static final Logger log = LoggerFactory.getLogger(UtxoManagement.class);

    public boolean isMine(Utxo utxo, Wallet wallet) {
//        log.info("Verifying utxo " + utxo + " for wallet " + wallet.getWalletId());
        if (utxo.getWalletId() == null) {
//            log.info("Ownership rejected");
            return false;
        } else {
//            log.info("Ownership confirmed");
            return (wallet.getWalletId().equals(utxo.getWalletId()));
        }
    }
}
