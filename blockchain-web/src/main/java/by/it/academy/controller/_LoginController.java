package by.it.academy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/login")
public class _LoginController {

//    @Autowired
//    UserService userService;

    @GetMapping
    public ModelAndView loginPage(ModelAndView modelAndView) {
        return modelAndView;
    }

//    @PostMapping
//    public RedirectView userLogin(
//            ModelAndView modelAndView,
//            @ModelAttribute Login login
//    ) {
//
//        String name = login.getInputName();
//        User userInput = userService.findUserByName(name);
//
//        String id = userInput.getUserId();
//
//        if (userInput.getUserPassword().equals(login.getInputPassword())) {
//            return new RedirectView("/blockchain-project/" + id + "/user-cabinet");
//        } else {
//            return new RedirectView("/blockchain-project/home");
//        }
//    }
}
