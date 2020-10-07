package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserCabinetController {

    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserCabinetController.class);

    @GetMapping("/{userId}/edit")
    public ModelAndView userEditView(
            ModelAndView modelAndView,
            @PathVariable String userId
    ) {
        log.info("User " + userId + " is going to edit the info");
        User user = userService.findUserById(userId);
        modelAndView.setViewName("user-edit");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/{userId}/update")
    public String userUpdate(
            ModelAndView modelAndView,
            @PathVariable String userId,
            @ModelAttribute User user
    ) {
        userService.updateUser(user);
        modelAndView.addObject("user", user);
        log.info("User initial: " + userService.findUserById(userId));
        log.info("User updates: " + user);
        if (UserService.getUsernameAuthUser().equals(user.getUserName())) {
            return "redirect:/{userId}/wallet-all";
        } else {
            return "redirect: /blockchain-web/logout";
        }
    }
}
