package by.it.academy.controller;

import by.it.academy.security.AuthenticationService;
import by.it.academy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/login-distributor")
public class LoginController {

    @Autowired
    UserService userService;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @GetMapping
    public String loginPage(ModelAndView modelAndView,
                            RedirectAttributes redirectAttributes) {

        String userId = AuthenticationService.userId;
        log.info("User with the id " + userId + " authorized and redirected to the cabinet");
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/{userId}/user-cabinet";
    }
}
