package myproject.starting;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Data
@Component
public class FirstBean {

    String message;

    @Autowired
    SecondBean secondBean;

    public void sendMessage(String message) {
        System.out.println(message);
    }

    public void testAutowired(String message, SecondBean secondBean) {
        System.out.println(secondBean.calc(3) + " " + message);
    }


}
