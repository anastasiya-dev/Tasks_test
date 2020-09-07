package by.it.academy.support;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TransactionStart {
    private String recipient;
    private String value;
}
