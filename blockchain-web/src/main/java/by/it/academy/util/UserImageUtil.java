package by.it.academy.util;

import by.it.academy.pojo.User;
import lombok.SneakyThrows;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class UserImageUtil {
    @SneakyThrows
    public static void saveToDisk(byte[] bytes, String fileName, User user) {
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
