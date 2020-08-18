package by.it.academy;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Logger;

//http://localhost:8080/hello-tomcat-1.0-SNAPSHOT/echo
// -1.0-SNAPSHOT - need to add as the folder is automatically titled this way
//8080 - tomcat port
//hello-tomcat - artifactId
//echo - value (very important to add slash!), URL pattern for servlet

@WebServlet(value = "/echo", name = "hello-world")
public class HelloWorldServlet extends HttpServlet {

    private int hitCounter;

    @Override
    public void init() throws ServletException {
        super.init();
        hitCounter = 0;
    }

    private static final Logger log = Logger.getLogger("HelloWorldServlet");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        hitCounter++;

        String contextPath = req.getContextPath();
        String info = req.getHeader("User-Agent");
        System.out.println("Context path: " + contextPath);
        log.info("Context path: " + contextPath);
        PrintWriter outGet = resp.getWriter();
        outGet.println(contextPath + ": Hello from my servlet to the user of " + info);
        outGet.println();
        outGet.println(Calendar.getInstance());
        outGet.println("Hit counter: " + hitCounter);
        outGet.println(req.getHeader("Accept-Encoding"));

        resp.setHeader("My-Name", "Anastasiya");
        log.info(Calendar.getInstance().toString());
        log.info(String.valueOf(hitCounter));
        countWriter(hitCounter);

        Cookie myCookie = new Cookie("My-Last-Name", "Kalach");
        //seconds - i.e. day in our case
        myCookie.setMaxAge(24 * 60 * 60);
        resp.addCookie(myCookie);

        HttpSession session = req.getSession();
        //objects can be only in session (not in parameters, init info etc)
        session.setAttribute("test", new Test());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter outPost = resp.getWriter();
        String username = req.getParameter("username");
        String phone_number = req.getParameter("phone_number");
        String e_mail = req.getParameter("e-mail");

        if (username == "" | (phone_number == "" && e_mail == "")) {
            outPost.println("Sorry, insufficient info");
            outPost.println("You need to add your name and either phone number or email");
            outPost.println();
        }
        outPost.println("Received user name: " + username);
        outPost.println("Received phone number: " + phone_number);
        outPost.println("Received e-mail: " + e_mail);
    }

    private void countWriter(int hitCounter) throws IOException {
        File file = new File("C:\\work\\hello-tomcat\\src\\main\\resources\\Hit counter.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(String.valueOf(Calendar.getInstance()).getBytes());
        fos.write("\n".getBytes());
        fos.write(("Counter: " + hitCounter).getBytes());
        fos.write("\n".getBytes());
        fos.write("\n".getBytes());
    }

    private void image(HttpServletResponse resp) throws IOException {
        resp.setContentType("image/jpeg");
        BufferedImage image = new BufferedImage(500, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setFont(new Font("Serif", Font.ITALIC, 48));
        graphics2D.setColor(Color.GREEN);
        graphics2D.drawString("Hello world!", 100, 100);
        ServletOutputStream out = resp.getOutputStream();
        ImageIO.write(image, "jpeg", out);
    }
}
