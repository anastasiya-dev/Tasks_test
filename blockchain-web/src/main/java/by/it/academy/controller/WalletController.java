package by.it.academy.controller;

import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
public class WalletController {

    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;

    @RequestMapping(value = "/{userId}/create-wallet", method = RequestMethod.GET)
    public ModelAndView createWallet(ModelAndView modelAndView,
                                     @PathVariable String userId) {
        Wallet wallet = walletService.createWallet(userId);
        walletService.saveWallet(wallet);
        modelAndView.addObject("wallet", wallet);
        modelAndView.setViewName("create-wallet");
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet-all", method = RequestMethod.GET)
    public ModelAndView viewAllTheWallets(ModelAndView modelAndView,
                                          @PathVariable String userId) {
        List<Wallet> wallets = walletService.getAllWalletsForUser(userId);
        Float sum = 0.0f;
        for (Wallet wallet : wallets) {
            wallet.setBalance(walletManagement.getBalance(wallet));
            sum += wallet.getBalance();
        }

        wallets.sort(Comparator.comparingInt(w -> (int) (w.getBalance() * (-10.0))));

        modelAndView.setViewName("wallet-all");
        modelAndView.addObject("wallets", wallets);
        modelAndView.addObject("sum", sum);
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}", method = RequestMethod.GET)
    public ModelAndView viewIndWallet(ModelAndView modelAndView,
                                      @PathVariable String userId,
                                      @PathVariable String walletId) {
        modelAndView.setViewName("ind-wallet");
        return modelAndView;
    }
}
