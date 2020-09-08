package by.it.academy.controller;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionInput;
import by.it.academy.pojo.Wallet;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {

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
        Float value = Float.valueOf(transactionStart.getValue());
        List<TransactionInput> inputs = new ArrayList<>();

        Transaction transaction
                = TransactionUtil.createTransaction(
                walletSender.getPublicKey(),
                walletRecipient.getPublicKey(),
                value,
                (ArrayList<TransactionInput>) inputs
        );

        List<Transaction> transactionList = transactionService.findAllTransactions();
        if (transactionList.size() == 0) {
            transaction.setTransactionId("0");
        } else {
            transaction.setTransactionId(
                    transactionList.get(transactionList.size() - 1).getTransactionId() + "0"
            );
        }
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

        if (privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            TransactionUtil.generateSignature(
                    transactionService.findTransactionById(transactionId), privateKey);
            return "redirect:/{userId}/user-cabinet";
        } else {
            transactionService.deleteTransaction(transactionService.findTransactionById(transactionId));
            return "redirect:/home";
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
        modelAndView.addObject("recipient", transaction.getReciepient());
        modelAndView.addObject("value", transaction.getValue());

        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        System.out.println(StringUtil.getStringFromKey(privateKey));

        modelAndView.setViewName("sign-transaction");
        return modelAndView;
    }
}
