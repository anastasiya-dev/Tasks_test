package myproject.starting;

import myproject.ApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        FirstBean firstBean = new FirstBean();
        firstBean.sendMessage("I'm sending message");
        SecondBean secondBean = new SecondBean();
        secondBean.setMyNumber(3);
        firstBean.testAutowired("hey", secondBean);

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        FirstBean correctFirstBean = (FirstBean) context.getBean("firstBean");
        SecondBean correctSecondBean = (SecondBean) context.getBean("secondBean");
        correctSecondBean.setMyNumber(4);
        correctFirstBean.testAutowired("he-hey", correctSecondBean);
    }
}
