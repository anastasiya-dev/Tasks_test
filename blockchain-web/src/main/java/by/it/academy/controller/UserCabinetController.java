package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.util.WalletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserCabinetController {

    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    String userId;

    @GetMapping("/{id}/user-cabinet")
    public String userCabinet(
            ModelAndView modelAndView,
            @PathVariable String id
    ) {
        userId = id;
        System.out.println(id);
        User user = userService.findUserById(id);
        System.out.println(user);
        modelAndView.addObject("user", user);
        return "redirect:/user-cabinet";
    }

    @GetMapping("/user-cabinet")
    public ModelAndView userCabinetMock(
            ModelAndView modelAndView
    ) {

        System.out.println(userId);
        return modelAndView;
    }

    @RequestMapping(value = "/create-wallet", method = RequestMethod.GET)
    public ModelAndView createWallet(ModelAndView modelAndView,
                                     @ModelAttribute User user,
                                     @ModelAttribute Wallet wallet) {
        wallet = WalletUtil.createWallet();
        System.out.println(wallet);
        user = userService.findUserById(userId);
        wallet.setUser(user);
        user.wallets.add(wallet);
        walletService.createNewWallet(wallet);
        System.out.println(wallet.getUser());
        modelAndView.addObject("wallet", wallet);
        return modelAndView;
    }

    @RequestMapping(value = "/wallet-all", method = RequestMethod.GET)
    public ModelAndView viewAllTheWallets(ModelAndView modelAndView) {
        List<Wallet> wallets = walletService.getAll(userId);
        modelAndView.setViewName("wallet-all");
        modelAndView.addObject("wallets", wallets);
        return modelAndView;
    }
}
