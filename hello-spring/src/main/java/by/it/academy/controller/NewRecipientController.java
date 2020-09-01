package by.it.academy.controller;

import by.it.academy.pojo.Recipient;
import by.it.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/new-recipient.html")
public class NewRecipientController {

    @Autowired
    UserService userService;

    @GetMapping
    public String showNewRecipient() {
        return "new-recipient";
    }

    @PostMapping
    public String createNewRecipient(
            @ModelAttribute Recipient recipient,
            Model model
    ) {
        System.out.println("New recipient: " + recipient);
        if (userService.createNewRecipient(recipient)) {
            return "redirect:home.html";
        } else {
            model.addAttribute("errorMessage", "Cannot create a new recipient");
            return "error-page";
        }
    }

}