package by.it.academy.service;

import by.it.academy.pojo.Recipient;
import by.it.academy.service.Message;

public interface MessageSender {

    void send(Recipient recipient, Message message);
}
