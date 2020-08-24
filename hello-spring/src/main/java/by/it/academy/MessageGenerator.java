package by.it.academy;

import org.springframework.stereotype.Component;

@Component
public class MessageGenerator {

    public Message generate(MessageType messageType) {
        Message message = new Message();
        message.setContent("Some content");
        message.setSubject("Some subject");
        return message;
    }



}
