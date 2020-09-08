package by.it.academy.controller;

import by.it.academy.pojo.User;
import by.it.academy.service.UserService;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
    public String createNewUser(
            @ModelAttribute User user,
            Model model,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        if (userService.findUserByName(user.getUserName()) != null) {
            return "redirect:login";
        }
        if (!user.getConfirmPassword().equals(user.getUserPassword())) {
            return "redirect:unconfirmed-password";
        } else {
            if (userService.createNewUser(user)) {
                byte[] bytes = file.getBytes();
                String fileName = "user_" + user.getUserId() + "_image.jpg";
                saveToDisk(bytes, fileName, user);
                redirectAttributes.addAttribute("userId", user.getUserId());
                return "redirect:/{userId}/user-cabinet";
            } else {
                return "redirect:home";
            }
        }
    }

    @GetMapping(value = "/unconfirmed-password")
    public ModelAndView unconfirmedPasswordError(ModelAndView modelAndView) {
        return modelAndView;
    }

    @SneakyThrows
    private void saveToDisk(byte[] bytes, String fileName, User user) {
        File usersDb = new File("C:\\work\\users\\");
        usersDb.mkdir();
        File indFolder = new File(usersDb, user.getUserId() + "\\");
        indFolder.mkdir();

        File picture = new File(indFolder, fileName);
        if (!picture.exists()) {
            picture.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picture));
        bos.write(bytes);
        bos.flush();
        bos.close();
    }
}
