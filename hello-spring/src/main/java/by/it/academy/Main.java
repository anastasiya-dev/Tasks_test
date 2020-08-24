package by.it.academy;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        //если создаем второй такой бин, то получим объект с тем же хешкодом
        //в рамках бинов контекста это тоже один бин, хотя мы и пишем, что как будто заново создаем новый
        //т.е. каждый раз мы получаем бин через синглтон модель по сути
        //scope singlton - by default, string pattern - analogy
        //если хотим, чтобы объекты добавлялись новые (с сохранением одного бина), то меняем скоуп на прототип

        final NotificationCommand notificationCommand =
                (NotificationCommand) context.getBean("notificationCommand");
        notificationCommand.setChannel(Channel.EMAIL);
        notificationCommand.setMessageType(MessageType.ORDER_DELIVERED);
        notificationCommand.setUserId("user");

        System.out.println(notificationCommand);

        final NotificationCommand notificationCommand2 =
                (NotificationCommand) context.getBean("notificationCommand");
        notificationCommand2.setChannel(Channel.SMS);
        notificationCommand2.setMessageType(MessageType.NEW_ORDER);
        notificationCommand2.setUserId("user");

        System.out.println(notificationCommand2);

        final NotificationCommandExecutor executor =
                (NotificationCommandExecutor) context.getBean("notificationCommandExecutor");
        executor.execute(notificationCommand);
        executor.execute(notificationCommand2);

        System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
    }
}
