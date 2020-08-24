package by.it.academy;

import org.springframework.stereotype.Component;

@Component
public class SmsMessageSender implements MessageSender {
    @Override
    public void send(Recipient recipient, Message message) {
        System.out.println("Send SMS");
    }
}
