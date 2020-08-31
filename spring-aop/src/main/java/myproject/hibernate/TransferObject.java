package myproject.hibernate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

@Data
@Component
@Entity
@NoArgsConstructor
public class TransferObject {

    @Id
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    String fromCard;
    String toCard;
    int amount;

    @Override
    public int hashCode() {
        final int hashCode = 7 * String.valueOf(Calendar.getInstance()).hashCode();
        return hashCode;
    }
}
