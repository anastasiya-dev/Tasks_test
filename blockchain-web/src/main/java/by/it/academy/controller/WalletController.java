package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.util.WalletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class WalletController {

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{userId}/create-wallet", method = RequestMethod.GET)
    public ModelAndView createWallet(ModelAndView modelAndView,
                                     @PathVariable String userId) {
        Wallet wallet = WalletUtil.createWallet();
        User user = userService.findUserById(userId);
        wallet.setUser(user);
        user.wallets.add(wallet);
        walletService.createNewWallet(wallet);
        modelAndView.addObject("wallet", wallet);
        modelAndView.setViewName("create-wallet");
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet-all", method = RequestMethod.GET)
    public ModelAndView viewAllTheWallets(ModelAndView modelAndView,
                                          @PathVariable String userId) {
        System.out.println("call my controller one");
        List<Wallet> wallets = walletService.getAll(userId);
        modelAndView.setViewName("wallet-all");
        modelAndView.addObject("wallets", wallets);
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}", method = RequestMethod.GET)
    public ModelAndView viewIndWallet(ModelAndView modelAndView,
                                      @PathVariable String userId,
                                      @PathVariable String walletId) {
        System.out.println("call my controller two");
        modelAndView.setViewName("ind-wallet");
        return modelAndView;
    }
}
