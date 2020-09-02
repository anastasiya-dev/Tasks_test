package by.it.academy.controller;

import by.it.academy.pojo.Recipient;
import by.it.academy.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class RecipientController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}/recipient.html")
    public ModelAndView showRecipientForm(
            @PathVariable String id,
            ModelAndView modelAndView
    ) {
        Recipient recipient = userService.get(id);
        modelAndView.addObject("user", recipient);
        modelAndView.setViewName("recipient");
        return modelAndView;
    }

    @PostMapping("/recipient.html")
    @SneakyThrows
    public String updateRecipient(
            @ModelAttribute Recipient recipient,
            @RequestParam("image") MultipartFile file
    ) {

        if (file == null) {
            byte[] bytes = file.getBytes();
//        String fileName = file.getOriginalFilename();
            String fileName = "recipient_" + recipient.getId() + "_image.jpg";
            saveToDisk(bytes, fileName, recipient);
        }

        userService.update(recipient);
        return "redirect:recipient-list.html";
    }

    @SneakyThrows
    private void saveToDisk(byte[] bytes, String fileName, Recipient recipient) {
        File recipientsDb = new File("C:\\work\\recipients\\");
        if (!recipientsDb.exists()) {
            recipientsDb.mkdir();
        }
        File indFolder = new File(recipientsDb, recipient.getId() + "\\");
        if (!indFolder.exists()) {
            indFolder.mkdir();
        }
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