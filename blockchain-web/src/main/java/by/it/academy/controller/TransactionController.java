package by.it.academy.controller;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionInput;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.support.TransactionStart;
import by.it.academy.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{userId}/wallet/{walletId}/create-transaction",
            method = RequestMethod.GET)
    public ModelAndView createTransactionPage(ModelAndView modelAndView,
                                              @PathVariable String userId,
                                              @PathVariable String walletId) {
        modelAndView.setViewName("create-transaction");
        return modelAndView;
    }


    @RequestMapping(value = "/{userId}/wallet/{walletId}/create-transaction",
            method = RequestMethod.POST)
    public String createWallet(ModelAndView modelAndView,
                                     @PathVariable String userId,
                                     @PathVariable String walletId,
                                     @ModelAttribute TransactionStart transactionStart) {

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
        transaction.setTransactionId("0");

        System.out.println(transaction);
        modelAndView.addObject("transaction", transaction);
        modelAndView.setViewName("create-transaction");
        return "redirect:sign-transaction";
    }

    
}
