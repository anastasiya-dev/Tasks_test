package by.it.academy.controller;

import by.it.academy.management.TransactionManagement;
import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.TransactionService;
import by.it.academy.service.WalletService;
import by.it.academy.support.PrivateKeyInput;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionUnconfirmedController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionManagement transactionManagement;
    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/unconfirmed",
            method = RequestMethod.GET)
    public ModelAndView viewUnconfirmed(ModelAndView modelAndView,
                                        @PathVariable String userId,
                                        @PathVariable String walletId
    ) {
        ArrayList<Transaction> unconfirmedTransactions = findUnconfirmed(walletId);
        float sum = 0.0f;
        for (Transaction transaction : unconfirmedTransactions) {
            sum += transaction.getValue();
        }

        modelAndView.setViewName("view-unconfirmed");
        modelAndView.addObject("sum", sum);
        modelAndView.addObject("transactions", unconfirmedTransactions);
        return modelAndView;
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/unconfirmed/sign-all",
            method = RequestMethod.POST)
    public String signAll(ModelAndView modelAndView,
                          @PathVariable String userId,
                          @PathVariable String walletId,
                          @ModelAttribute PrivateKeyInput privateKeyInput) {

        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();

        System.out.println("Sending private key to user:");
        System.out.println(StringUtil.getStringFromKey(walletService.findWalletById(walletId).getPrivateKey()));

        ArrayList<Transaction> unconfirmedTransactions = findUnconfirmed(walletId);

        float sum = 0.0f;
        for (Transaction transaction : unconfirmedTransactions) {
            sum += transaction.getValue();
        }

        if (!privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            return "redirect:/unauthorized-transaction";
        } else if (walletManagement.getBalance(walletSender) < sum) {
            return "redirect:/not-enough-funds";
        } else {

            for (Transaction transaction : unconfirmedTransactions) {
                transaction.setValue(transaction.getValue() * -1);
                transactionService.generateSignature(transaction, walletSender.getPrivateKey());
                transactionService.saveTransaction(transaction);
                Transaction signedTransaction = transactionService.findTransactionById(transaction.getTransactionId());
                walletManagement.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTransaction);
            }
            return "redirect:/{userId}/wallet/{walletId}";
        }
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/unconfirmed/delete-all",
            method = RequestMethod.GET)
    public String deleteAll(ModelAndView modelAndView,
                            @PathVariable String userId,
                            @PathVariable String walletId
    ) {
        ArrayList<Transaction> unconfirmedTransactions = findUnconfirmed(walletId);
        for (Transaction transaction : unconfirmedTransactions) {
            transactionService.deleteTransaction(transaction.getTransactionId());
        }
        return "redirect:/{userId}/wallet/{walletId}";
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/transaction/{transactionId}/delete-transaction",
            method = RequestMethod.GET)
    public String deleteTransaction(ModelAndView modelAndView,
                                    @PathVariable String userId,
                                    @PathVariable String walletId,
                                    @PathVariable String transactionId
    ) {
        Transaction transaction = transactionService.findTransactionById(transactionId);
        modelAndView.setViewName("view-unconfirmed");
        transactionService.deleteTransaction(transaction.getTransactionId());
        return "redirect:/{userId}/wallet/{walletId}/unconfirmed";
    }

    private ArrayList<Transaction> findUnconfirmed(String walletId) {
        List<Transaction> filteredTransactions = transactionManagement.getAllForWallet(walletId, true);
        ArrayList<Transaction> unconfirmedTransactions = new ArrayList<>();
        for (Transaction transaction : filteredTransactions) {
            if (transaction.getSenderId().equals(walletId)) {
                unconfirmedTransactions.add(transaction);
            }
        }
        return unconfirmedTransactions;
    }
}
