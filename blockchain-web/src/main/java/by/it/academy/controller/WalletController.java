package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.TransactionService;
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
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{userId}/create-wallet", method = RequestMethod.GET)
    public ModelAndView createWallet(ModelAndView modelAndView,
                                     @PathVariable String userId) {
        Wallet wallet = walletService.createWallet();
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
        List<Wallet> wallets = walletService.getAll(userId);
        Float sum = 0.0f;
        for (Wallet wallet : wallets) {
            wallet.setBalance(walletService.getBalance(wallet));
            sum += wallet.getBalance();
        }

        wallets.sort(Comparator.comparingInt(w -> Integer.valueOf((int) (w.getBalance() * (-100.0)))));
//        Comparator.reverseOrder().reversed();

        modelAndView.setViewName("wallet-all");
        modelAndView.addObject("wallets", wallets);

//        for (Wallet wallet : wallets) {
//            List<Transaction> transactions = transactionService.getAllForWallet(wallet.getWalletId());
//            for (Transaction transaction : transactions) {
//                sum += transaction.value;
//            }
//        }
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
