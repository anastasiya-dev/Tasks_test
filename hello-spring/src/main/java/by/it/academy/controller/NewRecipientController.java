package by.it.academy.controller;

import by.it.academy.pojo.Recipient;
import by.it.academy.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
    @SneakyThrows
    public String createNewRecipient(
            @ModelAttribute Recipient recipient,
            Model model,
            @RequestParam("image") MultipartFile file
    ) {
        System.out.println("New recipient: " + recipient);

        if (userService.createNewRecipient(recipient)) {
            byte[] bytes = file.getBytes();
            String fileName = "recipient_" + recipient.getId() + "_image.jpg";
            saveToDisk(bytes, fileName, recipient);
            return "redirect:home.html";
        } else {
            model.addAttribute("errorMessage", "Cannot create a new recipient");
            return "error-page";
        }
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