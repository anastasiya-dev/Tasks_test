package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.TransactionService;
import by.it.academy.service.UserService;
import by.it.academy.service.WalletService;
import by.it.academy.support.ChangePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserCabinetController {

    @Autowired
    UserService userService;

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
}
