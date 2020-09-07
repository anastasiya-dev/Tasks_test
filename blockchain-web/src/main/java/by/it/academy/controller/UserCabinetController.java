package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserCabinetController {

    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    String userId;

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
}
