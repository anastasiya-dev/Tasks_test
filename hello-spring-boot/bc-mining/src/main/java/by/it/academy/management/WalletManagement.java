package by.it.academy.management;

import by.it.academy.pojo.Utxo;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UtxoService;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class WalletManagement {

    @Autowired
    UtxoService utxoService;
    @Autowired
    UtxoManagement utxoManagement;


    public float getBalance(Wallet wallet) throws IOException {

        Logger logger = LoggerUtil.startLogging(WalletManagement.class.getName());
        logger.info("Getting balance for wallet " + wallet.getWalletId());
        float total = 0;
        ArrayList<Utxo> UTXOs = utxoService.findAllUTXOs();
        for (Utxo UTXO : UTXOs) {
            if (utxoManagement.isMine(UTXO, wallet)) { //if output belongs to me ( if coins belong to me )
                total += UTXO.getValue();
                logger.info(UTXO.toString());
            }
        }
        logger.info("Result: " + total);
        return total;
    }
}
