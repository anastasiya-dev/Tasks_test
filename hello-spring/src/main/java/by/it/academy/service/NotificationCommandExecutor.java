package by.it.academy.service;

import by.it.academy.pojo.Recipient;
import by.it.academy.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationCommandExecutor {

    @Autowired
    //очень важно передавать название класса именно так
    //иначе программа будет думать, что это стринг - чтобы обозначить референс на бин
    @Value("#{emailMessageSender}")
    by.it.academy.service.MessageSender MessageSender;

    @Value("100")
    int counter;

    @Autowired
    UserDao userRepository;
    @Autowired
    MessageGenerator messageGenerator;

    public boolean execute(NotificationCommand command) {
        System.out.println("Command to execute:");
        System.out.println(command);

        final Recipient recipient = (Recipient) userRepository.find(command.getUserId());
        final Message message = messageGenerator.generate(command.getMessageType());

        switch (command.getChannel()) {
            case SMS: {
                System.out.println("Not implemented");
                break;
            }
            case EMAIL: {
                MessageSender.send(recipient, message);
                break;
            }
            case VIBER: {
                System.out.println("Not implemented too");
                break;
            }
            default: {
                System.out.println("No channel");
            }
        }

        System.out.println("Executed successfully");
        return true;
    }

    public int getCount() {
        return counter;
    }
}
