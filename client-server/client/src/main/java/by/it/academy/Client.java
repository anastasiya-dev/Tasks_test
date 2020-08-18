package by.it.academy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
