package by.it.academy;

import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.support.UserStatus;
import by.it.academy.support.WalletStatus;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class BalanceTest {

    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;

    public float balance() throws IOException {
        Logger logger = LoggerUtil.startLogging(BalanceTest.class.getName());
        logger.info("Running balance test");

        float sum = 0.0f;
        ArrayList<User> all = (ArrayList<User>) userService.findAll(UserStatus.ACTIVE);
        for (User user : all) {
            ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletService.getAllWalletsForUser(user.getUserId(), WalletStatus.ACTIVE);
            for (Wallet wallet : wallets) {
                float balance = walletManagement.getBalance(wallet);
                sum += balance;
                logger.info("Wallet " + wallet.getWalletId() + ": " + balance);
            }
        }
        logger.info("Total: " + sum);
        return sum;
    }
}
