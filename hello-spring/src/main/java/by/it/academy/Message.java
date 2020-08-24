package by.it.academy;

public class Message {

    private String content;
    private String subject;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
