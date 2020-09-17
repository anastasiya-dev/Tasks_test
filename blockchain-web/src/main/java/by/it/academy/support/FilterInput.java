package by.it.academy.support;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class FilterInput {
    private String sender;
    private String recipient;
    private String valueMin;
    private String valueMax;
    private String status;
    private String dateStart;
    private String dateEnd;
}
