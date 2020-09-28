package by.it.academy.pojo;


import by.it.academy.support.MiningSessionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Component
@Getter
@Setter
@Table(name = "mining_session")
public class MiningSession {

    @Id
    @Column(name = "mining_session_id")
    private String miningSessionId;
    @Column(name = "wallet_id")
    private String walletId;
    @Column(name = "block_id")
    private String blockId;
    @Column(name = "miner_reward")
    private float minerReward;
    @Column(name = "session_request")
    private String sessionRequest;
    @Column(name = "session_start")
    private String sessionStart;
    @Column(name = "session_end")
    private String sessionEnd;
    @Column(name = "mining_session_status")
    private MiningSessionStatus miningSessionStatus;
    @Column(name = "consistency_confirmation")
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
