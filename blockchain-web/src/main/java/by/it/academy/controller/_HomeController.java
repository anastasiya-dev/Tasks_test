package by.it.academy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class _HomeController {
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView homePage(ModelAndView modelAndView) {
        modelAndView.addObject("greeting", "Welcome to our blockchain service!");
        return modelAndView;
    }
}
