package by.it.academy.controller;

import by.it.academy.pojo.Recipient;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView homePage(@ModelAttribute("recipient") Recipient recipient,
//                           BindingResult result,
                           ModelAndView modelAndView
//            ,
//                           RedirectAttributes redirectAttributes
    )
    {
        System.out.println("Call homePage");
        modelAndView.addObject("greeting", "I love Spring!");

        return modelAndView;

    }
}
