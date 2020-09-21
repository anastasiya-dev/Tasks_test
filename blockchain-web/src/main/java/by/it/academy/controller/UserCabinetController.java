package by.it.academy.controller;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.User;
import by.it.academy.service.TransactionService;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.support.ChangePassword;
import by.it.academy.support.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
public class UserCabinetController {

    @Autowired
    UserService userService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    WalletService walletService;

    @GetMapping("/{userId}/user-cabinet")
    public ModelAndView userCabinet(
            ModelAndView modelAndView,
            @PathVariable String userId
    ) {
        User user = userService.findUserById(userId);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user-cabinet");
        return modelAndView;
    }

    @GetMapping("/{userId}/user-cabinet/edit")
    public ModelAndView userEditView(
            ModelAndView modelAndView,
            @PathVariable String userId
    ) {
        User user = userService.findUserById(userId);
        modelAndView.setViewName("user-edit");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/{userId}/user-cabinet/update")
    public String userUpdate(
            ModelAndView modelAndView,
            @PathVariable String userId,
            @ModelAttribute User user
    ) {
        userService.updateUser(user);
        modelAndView.addObject("user", user);

        return "redirect:/{userId}/user-cabinet";
    }

    @GetMapping("/{userId}/change-password")
    public ModelAndView changePasswordView(
            ModelAndView modelAndView,
            @PathVariable String userId
    ) {
        modelAndView.setViewName("change-password");
        return modelAndView;
    }

    @PostMapping("/{userId}/change-password")
    public String updatePassword(
            ModelAndView modelAndView,
            @PathVariable String userId,
            RedirectAttributes redirectAttributes,
            @ModelAttribute ChangePassword changePassword
    ) {
        modelAndView.addObject("user", userService.findUserById(userId));
        User user = userService.findUserById(userId);
        if (!changePassword.getOldPassword().equals(user.getUserPassword())) {
            redirectAttributes.addAttribute("userId", userId);
            return "redirect:/{userId}/change-password";
        } else if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            return "redirect:/unconfirmed-password";
        } else {
            userService.updatePassword(userId, changePassword);
            redirectAttributes.addAttribute("userId", userId);
            return "redirect:/{userId}/user-cabinet";
        }
    }

    @GetMapping("/{userId}/statistics")
    public ModelAndView statistics(
            ModelAndView modelAndView,
            @PathVariable String userId
    ) {

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
        } catch (ArithmeticException e) {

        }

        return modelAndView;
    }
}
