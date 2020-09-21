package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
import by.it.academy.support.UserStatus;
import by.it.academy.util.UserImageUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignUpController {

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
//            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        if (userService.findUserByName(user.getUserName(), UserStatus.ACTIVE) != null) {
            return "redirect:login";
        }
        if (!user.getConfirmPassword().equals(user.getUserPassword())) {
            return "redirect:unconfirmed-password";
        } else {
            userService.saveUser(user);
//            byte[] bytes = file.getBytes();
//            String fileName = "user_" + user.getUserId() + "_image.jpg";
//            UserImageUtil.saveToDisk(bytes, fileName, user);
            redirectAttributes.addAttribute("userId", user.getUserId());
            return "redirect:/{userId}/user-cabinet";
        }
    }

    @GetMapping(value = "/unconfirmed-password")
    public ModelAndView unconfirmedPasswordError(ModelAndView modelAndView) {
        return modelAndView;
    }
}
