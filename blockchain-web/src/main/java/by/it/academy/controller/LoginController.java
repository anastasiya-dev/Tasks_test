package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
import by.it.academy.support.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping
    public ModelAndView loginPage(ModelAndView modelAndView) {
        return modelAndView;
    }

    @PostMapping
    public RedirectView userLogin(
            ModelAndView modelAndView,
            @ModelAttribute Login login
    ) {

        String name = login.getInputName();

        User userInput = userService.findUserByName(name);

        String id = userInput.getUserId();

        if (userInput.getUserPassword().equals(login.getInputPassword())) {
            return new RedirectView("/blockchain-web/" + id + "/user-cabinet");
        } else {
            return new RedirectView("/blockchain-web/home");
        }
    }
}
