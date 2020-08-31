package myproject.starting;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SecondBean {

    int myNumber;

    public int calc(int myNumber) {
        return myNumber * 5;
    }
}
