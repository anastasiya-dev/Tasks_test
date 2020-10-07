package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView homePage(ModelAndView modelAndView) {

        try {
            User user = userService.findUserByName(UserService.getUsernameAuthUser());
            modelAndView.addObject("userId", user.getUserId());
            log.info("Home page visit by " + user.getUserName());
        }
        catch (NullPointerException e){
            log.info("Home page visit by external user");
        }

        modelAndView.setViewName("home");
        return modelAndView;
    }
}
