package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
import by.it.academy.support.ChangePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChangePasswordController {

    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(ChangePasswordController.class);

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
}
