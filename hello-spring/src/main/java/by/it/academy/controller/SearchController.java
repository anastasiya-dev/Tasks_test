package by.it.academy.controller;

import by.it.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

    @Autowired
    UserService userService;

    @GetMapping("/search.html")
    public ModelAndView search(
            @RequestParam String search,
            ModelAndView modelAndView) {
        System.out.println("Search: " + search);

        modelAndView.addObject("users", userService.search(search));
        modelAndView.setViewName("search-result");
        return modelAndView;
    }
}