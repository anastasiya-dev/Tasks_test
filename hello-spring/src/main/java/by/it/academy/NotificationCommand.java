package by.it.academy;

public class NotificationCommand {

    private String userId;
    private MessageType messageType;
    private Channel channel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "NotificationCommand{" +
                "userId='" + userId + '\'' +
                ", messageType=" + messageType +
                ", channel=" + channel +
                '}';
    }
}
