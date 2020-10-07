package by.it.academy.controller;

import by.it.academy.pojo.Transaction;
import by.it.academy.service.TransactionService;
import by.it.academy.service.WalletService;
import by.it.academy.support.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class StatisticsController {

    @Autowired
    WalletService walletService;
    @Autowired
    TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    @GetMapping("/{userId}/statistics")
    public ModelAndView statistics(
            ModelAndView modelAndView,
            @PathVariable String userId
    ) {

        log.info("Preparing statistics for user " + userId);
        ArrayList<Transaction> allTransactions = transactionService.findAllTransactions();
        float senderRewards = 0.0f;
        float minerRewards = 0.0f;
        for (Transaction transaction : allTransactions) {
            if (walletService.findWalletById(transaction.getRecipientId()).getUserId().equals(userId)) {
                if (transaction.getTransactionStatus().equals(TransactionStatus.REWARD_M)) {
                    minerRewards += transaction.getValue();
                }
                if (transaction.getTransactionStatus().equals(TransactionStatus.REWARD_S)) {
                    senderRewards += transaction.getValue();
                }
            }
        }
        float totalIncome = senderRewards + minerRewards;
        try {
            modelAndView.addObject("minerReward", Math.round(minerRewards / totalIncome * 100));
            modelAndView.addObject("senderReward", Math.round(senderRewards / totalIncome * 100));
            modelAndView.setViewName("statistics");
            modelAndView.addObject("totalIncome", totalIncome);
            log.info("Chart presented");
        } catch (ArithmeticException e) {
            log.info("No income yet");
        }
        return modelAndView;
    }
}
