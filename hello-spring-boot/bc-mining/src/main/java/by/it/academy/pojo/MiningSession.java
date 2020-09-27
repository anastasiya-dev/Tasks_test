package by.it.academy.pojo;


import by.it.academy.support.MiningSessionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Component
@Getter
@Setter
public class MiningSession {

    @Id
    private String miningSessionId;
    private String walletId;
    private String blockId;
    private float minerReward;
    private String sessionRequest;
    private String sessionStart;
    private String sessionEnd;
    private MiningSessionStatus miningSessionStatus;
    private String consistencyConfirmation;

    @Override
    public String toString() {
        return "MiningSession{" +
                "miningSessionId='" + miningSessionId + '\'' +
                ", walletId='" + walletId + '\'' +
                ", blockId='" + blockId + '\'' +
                ", minerReward=" + minerReward +
                ", sessionRequest='" + sessionRequest + '\'' +
                ", sessionStart='" + sessionStart + '\'' +
                ", sessionEnd='" + sessionEnd + '\'' +
                ", miningSessionStatus=" + miningSessionStatus +
                ", consistencyConfirmation='" + consistencyConfirmation + '\'' +
                '}';
    }
}
