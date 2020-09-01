package by.it.academy.service;

import lombok.Data;

@Data
public class NotificationCommand {

    private String userId;
    private MessageType messageType;
    private Channel channel;
}
