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
    private String operationType;
    private String dateStart;
    private String dateEnd;

    @Override
    public String toString() {
        return "FilterInput{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", valueMin='" + valueMin + '\'' +
                ", valueMax='" + valueMax + '\'' +
                ", status='" + operationType + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                '}';
    }
}
