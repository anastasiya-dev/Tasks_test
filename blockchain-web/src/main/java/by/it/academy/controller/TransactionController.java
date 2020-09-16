package by.it.academy.controller;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UtxoService;
import by.it.academy.service.TransactionService;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.support.PrivateKeyInput;
import by.it.academy.support.TransactionStart;
import by.it.academy.util.StringUtil;
import by.it.academy.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.PrivateKey;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    UtxoService utxoService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

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
        Wallet walletRecipient = walletService.findWalletById(transactionStart.getRecipient());
        System.out.println("wallet recipient found: ");
        System.out.println(walletRecipient);
        Float value = Float.valueOf(transactionStart.getValue());

        Transaction transaction
                = transactionService.createTransaction(
                walletSender.getWalletId(),
                transactionStart.getRecipient(),
                value
        );

        transactionService.createNewTransaction(transaction);
        System.out.println(transaction);
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
        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        Transaction transaction = transactionService.findTransactionById(transactionId);
        if (!privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            System.out.println("#Unauthorized");
            return "redirect:/home";
        } else if (walletService.getBalance(walletSender) < transaction.getValue()) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return "redirect:/home";
        } else {
            TransactionUtil.generateSignature(transaction, walletSender.privateKey);
            transactionService.createNewTransaction(transaction);
            Transaction signedTr = transactionService.findTransactionById(transaction.transactionId);
            walletService.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTr);
            return "redirect:/{userId}/user-cabinet";
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
        modelAndView.addObject("sender", transaction.getSender());
        modelAndView.addObject("recipient", transaction.getRecipient());
        modelAndView.addObject("value", transaction.getValue());

        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        System.out.println(StringUtil.getStringFromKey(privateKey));

        modelAndView.setViewName("sign-transaction");
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}/transaction-all", method = RequestMethod.GET)
    public ModelAndView viewAllTheWallets(ModelAndView modelAndView,
                                          @PathVariable String userId,
                                          @PathVariable String walletId) {

        List<Transaction> transactions = transactionService.getAllForWallet(walletId);
        Float sum = 0.0f;
        for (Transaction transaction : transactions) {
            sum += transaction.value;
        }
        modelAndView.setViewName("transaction-all");
        modelAndView.addObject("transactions", transactions);
        modelAndView.addObject("sum", sum);
        return modelAndView;
    }
}