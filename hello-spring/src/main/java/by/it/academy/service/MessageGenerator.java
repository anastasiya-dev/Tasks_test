package by.it.academy.service;

import by.it.academy.service.Message;
import by.it.academy.service.MessageType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MessageGenerator implements ApplicationContextAware, DisposableBean {
    private ApplicationContext context;

    public Message generate(MessageType messageType) {
        return (Message) context.getBean("message", "Some content", "Some subject");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Bean message generator has been destroyed");
    }
}
