package by.it.academy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static final Logger log = Logger.getLogger("HelloWorldServlet");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        System.out.println("Context path: " + contextPath);
        log.info("Context path: " + contextPath);
        PrintWriter writer = resp.getWriter();
        writer.println(contextPath + ": Hello from my servlet \n" + Calendar.getInstance());
    }
}
