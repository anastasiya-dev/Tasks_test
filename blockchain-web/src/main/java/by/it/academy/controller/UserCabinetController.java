package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.util.WalletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView createWallet(ModelAndView modelAndView) {
        Wallet wallet = WalletUtil.createWallet();
        System.out.println(wallet);
        User user = userService.findUserById(userId);
        wallet.setUser(user);
        user.wallets.add(wallet);
        walletService.createNewWallet(wallet);
        System.out.println(wallet.getUser());
        return modelAndView;
    }
}
