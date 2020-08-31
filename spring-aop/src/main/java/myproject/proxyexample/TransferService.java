package myproject.proxyexample;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TransferService {

    public int transfer(TransferData transferData) {
        int code = new Random().nextInt(3);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The transfer from ").append(transferData.getFromCard())
                .append(" to ").append(transferData.getToCard())
                .append(" of the amount ").append(transferData.getAmount())
                .append(" was ").append(Status.getValueByCode(code));
        System.out.println(stringBuilder.toString());
        return code;
    }
}
