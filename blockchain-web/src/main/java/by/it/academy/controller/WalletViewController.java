package by.it.academy.controller;

import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.WalletService;
import by.it.academy.support.WalletStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
public class WalletViewController {

    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;
    private static final Logger log = LoggerFactory.getLogger(WalletViewController.class);

    @RequestMapping(value = "/{userId}/create-wallet", method = RequestMethod.GET)
    public ModelAndView createWallet(ModelAndView modelAndView,
                                     @PathVariable String userId) {
        Wallet wallet = walletService.createWallet(userId);
        walletService.saveWallet(wallet);
        log.info("User " + userId + " created new wallet");
        log.info(String.valueOf(wallet));
        modelAndView.addObject("wallet", wallet);
        modelAndView.setViewName("create-wallet");
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet-all", method = RequestMethod.GET)
    public ModelAndView viewAllTheWallets(ModelAndView modelAndView,
                                          @PathVariable String userId) {
        List<Wallet> wallets = walletService.getAllWalletsForUser(userId, WalletStatus.ACTIVE);
        Float sum = 0.0f;
        for (Wallet wallet : wallets) {
            wallet.setBalance((float) Math.round(walletManagement.getBalance(wallet) * 10.0) / 10.0f);
            sum += wallet.getBalance();
        }

        wallets.sort(Comparator.comparingInt(w -> (int) (w.getBalance() * (-10.0))));
        log.info("Showing all the wallets of the user " + userId);
        modelAndView.setViewName("wallet-all");
        modelAndView.addObject("wallets", wallets);
        modelAndView.addObject("sum", (float) Math.round(sum * 10.0) / 10.0f);
        return modelAndView;
    }
}
