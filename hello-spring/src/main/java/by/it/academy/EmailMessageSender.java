package by.it.academy;

import org.springframework.stereotype.Component;

@Component
public class EmailMessageSender implements MessageSender {

    @Override
    public void send(Recipient recipient, Message message) {
        if (recipient.getEmailAddress() == null
                || "".equals(recipient.getEmailAddress())) {
            throw new IllegalArgumentException("Email address cannot be empty");
        }
        System.out.println("Sent by email to: " + recipient);
        System.out.println("Message content: " + message);
    }
}
