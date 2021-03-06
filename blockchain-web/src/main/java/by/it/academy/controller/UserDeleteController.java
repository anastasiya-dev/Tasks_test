package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.support.UserStatus;
import by.it.academy.support.WalletStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class UserDeleteController {

    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;

    private static final Logger log = LoggerFactory.getLogger(UserDeleteController.class);

    @RequestMapping(
            value = "/{userId}/delete",
            method = RequestMethod.GET)
    public String deleteUser(ModelAndView modelAndView,
                             @PathVariable String userId) {
        log.info("User " + userId + " is going to delete profile");
        ArrayList<Wallet> wallets = (ArrayList<Wallet>) walletService.getAllWalletsForUser(userId, WalletStatus.ACTIVE);
        if (wallets.isEmpty()) {
            log.info("Accepted");
            return "redirect: /blockchain-web/{userId}/delete-user-confirm";
        } else {
            log.warn("Paused. Need to check the wallets");
            return "redirect: /blockchain-web/{userId}/delete-user-check-wallets";
        }
    }

    @RequestMapping(
            value = "/{userId}/delete-user-confirm",
            method = RequestMethod.GET)
    public ModelAndView deleteUserConfirm(ModelAndView modelAndView,
                                          @PathVariable String userId) {
        modelAndView.setViewName("delete-user-confirm");
        return modelAndView;
    }

    @RequestMapping(
            value = "/{userId}/delete-user-confirm-process",
            method = RequestMethod.GET)
    public String deleteUserProcess(ModelAndView modelAndView,
                                    @PathVariable String userId) {
        User user = userService.findUserById(userId);
        log.info("User deleted: " + user);
        user.setUserStatus(UserStatus.DELETED);
        userService.delete(user);
        return "redirect: /blockchain-web/logout";
    }

    @RequestMapping(
            value = "/{userId}/delete-user-check-wallets",
            method = RequestMethod.GET)
    public ModelAndView deleteUserCheckWallets(ModelAndView modelAndView,
                                               @PathVariable String userId) {
        modelAndView.setViewName("delete-user-check-wallets");
        return modelAndView;
    }
}
