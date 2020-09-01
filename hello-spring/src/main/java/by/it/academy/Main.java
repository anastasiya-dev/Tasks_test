package by.it.academy;

import by.it.academy.pojo.Recipient;
import by.it.academy.repository.UserDao;
import by.it.academy.service.Channel;
import by.it.academy.service.MessageType;
import by.it.academy.service.NotificationCommand;
import by.it.academy.service.NotificationCommandExecutor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class Main {
    static AnnotationConfigApplicationContext context;

    public static void main(String[] args) throws InterruptedException {

        context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        //если создаем второй такой бин, то получим объект с тем же хешкодом
        //в рамках бинов контекста это тоже один бин, хотя мы и пишем, что как будто заново создаем новый
        //т.е. каждый раз мы получаем бин через синглтон модель по сути
        //scope singlton - by default, string pattern - analogy
        //если хотим, чтобы объекты добавлялись новые (с сохранением одного бина), то меняем скоуп на прототип

        createUser("user1");
        createUser("user2");

        final NotificationCommand notificationCommand1 = createCommand("user1");
        final NotificationCommand notificationCommand2 = createCommand("user2");

        final NotificationCommandExecutor executor =
                (NotificationCommandExecutor) context.getBean("notificationCommandExecutor");
        System.out.println("Executor count: " + executor.getCount());
        executor.execute(notificationCommand1);
        executor.execute(notificationCommand2);


        System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
        context.close();
        Thread.sleep(3000);
    }

    private static NotificationCommand createCommand(String userId) {
        NotificationCommand notificationCommand =
                (NotificationCommand) context.getBean("notificationCommand");
        notificationCommand.setChannel(Channel.EMAIL);
        notificationCommand.setMessageType(MessageType.ORDER_DELIVERED);
        notificationCommand.setUserId(userId);
        return notificationCommand;
    }

    private static void createUser(String userId) {
        UserDao userRepository = (UserDao) context.getBean("userRepository");
        Recipient recipient = new Recipient(null, userId, userId + "@mail.ru", null);
        userRepository.create(recipient);
    }
}
