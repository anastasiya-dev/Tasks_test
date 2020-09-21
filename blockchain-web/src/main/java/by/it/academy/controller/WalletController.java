package by.it.academy.controller;

import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.TransactionService;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.support.PrivateKeyInput;
import by.it.academy.support.TransactionStatus;
import by.it.academy.support.UserStatus;
import by.it.academy.support.WalletStatus;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.PrivateKey;
import java.util.Comparator;
import java.util.List;

@Controller
public class WalletController {

    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;

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
        List<Wallet> wallets = walletService.getAllWalletsForUser(userId, WalletStatus.ACTIVE);
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


    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/delete",
            method = RequestMethod.POST)
    public String deleteWallet(ModelAndView modelAndView,
                               @PathVariable String userId,
                               @PathVariable String walletId,
                               @ModelAttribute PrivateKeyInput privateKeyInput) {

        modelAndView.setViewName("delete-wallet");
        Transaction transaction
                = transactionService.createTransaction(
                walletId,
                walletService.getAllWalletsForUser
                        (
                                userService.findUserByName("Genesis1", UserStatus.ACTIVE).getUserId(),
                                WalletStatus.ACTIVE
                        ).get(0).getWalletId(),
                walletManagement.getBalance(walletService.findWalletById(walletId))
        );

        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        if (!privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            return "redirect:/unauthorized-transaction";
        } else {
            transactionService.saveTransaction(transaction);
            transactionService.generateSignature(transaction, walletSender.getPrivateKey());
            transaction.setTransactionStatus(TransactionStatus.WALLET_DELETION);
            Transaction signedTransaction = transactionService.updateTransaction(transaction);
            transactionService.walletDeleteTransaction(signedTransaction);
            walletManagement.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTransaction);
            walletService.delete(walletId);
            return "redirect:/{userId}/wallet/{walletId}";
        }
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/delete",
            method = RequestMethod.GET)
    public ModelAndView deleteWalletPage(ModelAndView modelAndView,
                                         @PathVariable String userId,
                                         @PathVariable String walletId,
                                         @ModelAttribute PrivateKeyInput privateKeyInput) {

        System.out.println("Sending private key to user:");
        System.out.println(StringUtil.getStringFromKey(walletService.findWalletById(walletId).getPrivateKey()));
        modelAndView.addObject("balance",
                walletManagement.getBalance(walletService.findWalletById(walletId)));
        modelAndView.setViewName("delete-wallet");
        return modelAndView;
    }
}
