package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
import by.it.academy.support.UserStatus;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignUpController {

    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    UserService userService;

    @GetMapping(value = "/signup")
    public ModelAndView signUpPage(ModelAndView modelAndView) {
        return modelAndView;
    }

    @PostMapping(value = "/signup")
    @SneakyThrows
    public String signUpUser(
            @ModelAttribute User user,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        log.info("Signing up new user");
        if (userService.findUserByName(user.getUserName(), UserStatus.ACTIVE) != null) {
            log.warn("Existing user attemped signup");
            return "redirect:login";
        }
        if (!user.getConfirmPassword().equals(user.getUserPassword())) {
            return "redirect:unconfirmed-password";
        } else {
            userService.saveUser(user);
            redirectAttributes.addAttribute("userId", user.getUserId());
            return "redirect:/{userId}/user-cabinet";
        }
    }

    @GetMapping(value = "/unconfirmed-password")
    public ModelAndView unconfirmedPasswordError(ModelAndView modelAndView) {
        return modelAndView;
    }
}
