package by.it.academy.controller;

import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.TransactionService;
import by.it.academy.service.WalletService;
import by.it.academy.support.PrivateKeyInput;
import by.it.academy.support.TransactionStart;
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

    private static final Logger log = LoggerFactory.getLogger(TransactionCreateController.class);

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
        log.info("User " + userId + " via wallet " + walletId + " is starting transaction: ");
        log.info(String.valueOf(transactionStart));
        Wallet walletSender = walletService.findWalletById(walletId);

        try {
            Transaction transaction
                    = transactionService.createTransaction(
                    walletSender.getWalletId(),
                    transactionStart.getRecipient(),
                    Float.parseFloat(transactionStart.getValue())
            );
            log.info("Transaction saved");
            transactionService.saveTransaction(transaction);
            String transactionId = transaction.getTransactionId();
            redirectAttributes.addAttribute("transactionId", transactionId);
            modelAndView.setViewName("create-transaction");
            return "redirect:/{userId}/wallet/{walletId}/transaction/{transactionId}/sign-transaction";
        }
        catch (Exception e){
            {
                log.warn("Incorrect transaction input");
                return "redirect:/{userId}/wallet/{walletId}/transaction/incorrect-input";
            }
        }
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}/transaction/incorrect-input",
            method = RequestMethod.GET)
    public ModelAndView incorrectTransactionInput(ModelAndView modelAndView,
                                                  @PathVariable String userId,
                                                  @PathVariable String walletId) {
        modelAndView.setViewName("incorrect-input");
        return modelAndView;
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

        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        Transaction transaction = transactionService.findTransactionById(transactionId);

        log.info("User " + userId + " via wallet " + walletId + " is signing transaction: ");
        log.info(String.valueOf(transaction));

        if (!privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            log.warn("Denied. Unauthorized");
            return "redirect:/{userId}/wallet/{walletId}/unauthorized-transaction";
        } else if (walletManagement.getBalance(walletSender) < transaction.getValue()) {
            log.warn("Denied. Not enough funds");
            return "redirect:/{userId}/wallet/{walletId}/not-enough-funds";
        } else {
            transactionService.generateSignature(transaction, walletSender.getPrivateKey());
            Transaction signedTransaction = transactionService.updateTransaction(transaction);
            walletManagement.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTransaction);
            log.info("Accepted");
            return "redirect:/{userId}/wallet-all";
        }
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}/unauthorized-transaction",
            method = RequestMethod.GET)
    public ModelAndView unauthorizedTransaction(ModelAndView modelAndView,
                                                  @PathVariable String userId,
                                                  @PathVariable String walletId) {
        modelAndView.setViewName("unauthorized-transaction");
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}/not-enough-funds",
            method = RequestMethod.GET)
    public ModelAndView notEnoughFunds(ModelAndView modelAndView,
                                                @PathVariable String userId,
                                                @PathVariable String walletId) {
        modelAndView.setViewName("not-enough-funds");
        return modelAndView;
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

        log.info("Sending private key to user:");
        log.info(StringUtil.getStringFromKey(walletService.findWalletById(walletId).getPrivateKey()));

        modelAndView.setViewName("sign-transaction");
        return modelAndView;
    }
}