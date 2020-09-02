package by.it.academy.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    @GetMapping(
            value = "/{id}/image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable String id) throws IOException {
        File file = new File("C:\\work\\recipients\\" + id + "\\" + "recipient_" + id + "_image.jpg");
        InputStream in = new FileInputStream(file);
        return IOUtils.toByteArray(in);
    }
}
