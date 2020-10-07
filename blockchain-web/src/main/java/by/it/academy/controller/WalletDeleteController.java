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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.PrivateKey;

@Controller
public class WalletDeleteController {

    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(WalletDeleteController.class);

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/delete",
            method = RequestMethod.POST)
    public String deleteWallet(ModelAndView modelAndView,
                               @PathVariable String userId,
                               @PathVariable String walletId,
                               @ModelAttribute PrivateKeyInput privateKeyInput) {

        log.info("User " + userId + " is going to delete the wallet " + walletId);
        modelAndView.setViewName("delete-wallet");

        if (userService.findUserById(walletService.findWalletById(walletId).getUserId()).getUserName().contains("Genesis")) {
            log.warn("Attempting to delete genesis wallet. Denied");
            return "redirect:/{userId}/wallet-all";
        } else {
            Transaction transaction
                    = transactionService.createTransaction(
                    walletId,
                    walletService.getAllWalletsForUser
                            (
                                    userService.findUserByName("Genesis1").getUserId(),
                                    WalletStatus.ACTIVE
                            ).get(0).getWalletId(),
                    walletManagement.getBalance(walletService.findWalletById(walletId))
            );

            Wallet walletSender = walletService.findWalletById(walletId);
            PrivateKey privateKey = walletSender.getPrivateKey();
            log.info("Sending private key to user:");
            log.info(StringUtil.getStringFromKey(walletService.findWalletById(walletId).getPrivateKey()));
            if (!privateKeyInput.getPrivateKeyString()
                    .equals(StringUtil.getStringFromKey(privateKey))) {
                log.warn("Denied. Unauthorized");
                return "redirect:/unauthorized-transaction";
            } else {
                transactionService.saveTransaction(transaction);
                transactionService.generateSignature(transaction, walletSender.getPrivateKey());
                transaction.setTransactionStatus(TransactionStatus.WALLET_DELETION);
                Transaction signedTransaction = transactionService.updateTransaction(transaction);
                transactionService.walletDeleteTransaction(signedTransaction);
                walletManagement.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTransaction);
                walletService.delete(walletId);
                log.info("Accepted");
                log.info("Clearing transaction: " + transaction);
                return "redirect:/{userId}/wallet-all";
            }
        }
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/delete",
            method = RequestMethod.GET)
    public ModelAndView deleteWalletPage(ModelAndView modelAndView,
                                         @PathVariable String userId,
                                         @PathVariable String walletId,
                                         @ModelAttribute PrivateKeyInput privateKeyInput) {

        log.info("Sending private key to user:");
        log.info(StringUtil.getStringFromKey(walletService.findWalletById(walletId).getPrivateKey()));
        modelAndView.addObject("balance",
                walletManagement.getBalance(walletService.findWalletById(walletId)));
        modelAndView.setViewName("delete-wallet");
        return modelAndView;
    }
}

