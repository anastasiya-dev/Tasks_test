package by.it.academy.controller;

import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.TransactionService;
import by.it.academy.service.WalletService;
import by.it.academy.support.PrivateKeyInput;
import by.it.academy.support.TransactionStart;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.PrivateKey;

@Controller
public class TransactionCreateController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;

    @RequestMapping(value = "/{userId}/wallet/{walletId}/create-transaction",
            method = RequestMethod.GET)
    public ModelAndView transactionPage(ModelAndView modelAndView,
                                        @PathVariable String userId,
                                        @PathVariable String walletId) {
        modelAndView.setViewName("create-transaction");
        return modelAndView;
    }


    @RequestMapping(value = "/{userId}/wallet/{walletId}/create-transaction",
            method = RequestMethod.POST)
    public String createTransaction(ModelAndView modelAndView,
                                    @PathVariable String userId,
                                    @PathVariable String walletId,
                                    @ModelAttribute TransactionStart transactionStart,
                                    RedirectAttributes redirectAttributes
    ) {

        Wallet walletSender = walletService.findWalletById(walletId);

        Transaction transaction
                = transactionService.createTransaction(
                walletSender.getWalletId(),
                transactionStart.getRecipient(),
                Float.parseFloat(transactionStart.getValue())
        );

        transactionService.saveTransaction(transaction);

        String transactionId = transaction.getTransactionId();
        redirectAttributes.addAttribute("transactionId", transactionId);
        modelAndView.setViewName("create-transaction");
        return "redirect:/{userId}/wallet/{walletId}/transaction/{transactionId}/sign-transaction";
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/transaction/{transactionId}/sign-transaction",
            method = RequestMethod.POST)
    public String signTransaction(ModelAndView modelAndView,
                                  @PathVariable String userId,
                                  @PathVariable String walletId,
                                  @PathVariable String transactionId,
                                  RedirectAttributes redirectAttributes,
                                  @ModelAttribute PrivateKeyInput privateKeyInput) {

        modelAndView.setViewName("sign-transaction");
        System.out.println("Sending private key to user:");
        System.out.println(StringUtil.getStringFromKey(walletService.findWalletById(walletId).getPrivateKey()));

        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        Transaction transaction = transactionService.findTransactionById(transactionId);
        if (!privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            return "redirect:/unauthorized-transaction";
        } else if (walletManagement.getBalance(walletSender) < transaction.getValue()) {
            return "redirect:/not-enough-funds";
        } else {
            transactionService.generateSignature(transaction, walletSender.getPrivateKey());
            Transaction signedTransaction = transactionService.updateTransaction(transaction);
            walletManagement.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTransaction);
            return "redirect:/{userId}/wallet/{walletId}";
        }
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/transaction/{transactionId}/sign-transaction",
            method = RequestMethod.GET)
    public ModelAndView signTransactionPage(ModelAndView modelAndView,
                                            @PathVariable String userId,
                                            @PathVariable String walletId,
                                            @PathVariable String transactionId,
                                            RedirectAttributes redirectAttributes,
                                            @ModelAttribute PrivateKeyInput privateKeyInput) {

        Transaction transaction = transactionService.findTransactionById(transactionId);
        modelAndView.addObject("transaction", transaction);

        modelAndView.setViewName("sign-transaction");
        return modelAndView;
    }
}