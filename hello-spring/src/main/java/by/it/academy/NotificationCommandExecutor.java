package by.it.academy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandExecutor {

    @Autowired
    //очень важно передавать название класса именно так
    //иначе программа будет думать, что это стринг - чтобы обозначить референс на бин
    @Value("#{emailMessageSender}")
    MessageSender MessageSender;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageGenerator messageGenerator;

    public boolean execute(NotificationCommand command) {
        System.out.println("Command to execute:");
        System.out.println(command);

        final Recipient recipient = userRepository.find(command.getUserId());
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
}
