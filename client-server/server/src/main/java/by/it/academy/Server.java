package by.it.academy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while(true){
                Socket accept = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(accept.getOutputStream());
                dos.writeUTF("Hello " + Calendar.getInstance().toString());
                dos.flush();
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
