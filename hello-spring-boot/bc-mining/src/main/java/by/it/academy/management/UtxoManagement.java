package by.it.academy.management;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.util.LoggerUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class UtxoManagement {

    public boolean isMine(Utxo utxo, Wallet wallet) throws IOException {
        Logger logger = LoggerUtil.startLogging(WalletManagement.class.getName());
        logger.info("Verifying utxo " + utxo + " for wallet " + wallet.getWalletId());

        if (utxo.getWalletId() == null) {
            logger.info("Ownership rejected");
            return false;
        } else {
            logger.info("Ownership confirmed");
            return (wallet.getWalletId().equals(utxo.getWalletId()));
        }
    }
}
