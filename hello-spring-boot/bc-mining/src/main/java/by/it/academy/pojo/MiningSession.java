package by.it.academy.pojo;


import by.it.academy.support.MiningSessionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Component
@Getter
@Setter
public class MiningSession {

    @Id
    private String minerId;
    private String walletId;
    private String blockId;
    private String minerReward;
    private String sessionStart;
    private String sessionEnd;
    private String miningSessionStatus;

    @Override
    public String toString() {
        return "MiningSession{" +
                "minerId='" + minerId + '\'' +
                ", walletId='" + walletId + '\'' +
                ", blockId='" + blockId + '\'' +
                ", minerReward=" + minerReward +
                ", sessionStart=" + sessionStart +
                ", sessionEnd=" + sessionEnd +
                ", miningSessionStatus=" + miningSessionStatus +
                '}';
    }
}
