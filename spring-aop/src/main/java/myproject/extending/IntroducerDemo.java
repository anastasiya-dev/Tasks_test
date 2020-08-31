package myproject.extending;

import myproject.ApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IntroducerDemo {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        DoTask doTask = context.getBean("taskService", DoTask.class);
        doTask.doMainJob();
        ((DoExtendedTask) doTask).doExtraJob();
    }
}
