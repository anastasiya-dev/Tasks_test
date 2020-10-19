package analyzer;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class TransactionRecord {

    private String ID;
    private LocalDateTime date;
    private float amount;
    private String merchant;
    private TransactionType type;
    private Optional<String> relatedTransaction;

    public void setID(String ID) {
        this.ID = ID.trim();
    }

    public void setDate(String date) {
        this.date = LocalDateTime.parse(date.trim(), TransactionAnalyzer.FORMATTER);
    }

    public void setAmount(String amount) {
        this.amount = Float.parseFloat(amount);
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant.trim();
    }

    public void setType(String type) {
        this.type = TransactionType.valueOf(type.trim());
    }

    public void setRelatedTransaction(String relatedTransaction) {
        this.relatedTransaction = Optional.of(relatedTransaction.trim());
    }
}
