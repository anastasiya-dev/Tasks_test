package analyzer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionAnalyzer {

    private static int transactionsNumber;
    private static float transactionsSum;
    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static int getTransactionsNumber() {
        return transactionsNumber;
    }

    public static float getSum() {
        return transactionsSum;
    }

    public static ArrayList<TransactionRecord> transactionsFilter(
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            String merchant,
            ArrayList<TransactionRecord> transactionRecords) {

        transactionsNumber = 0;
        transactionsSum = 0;
        ArrayList<TransactionRecord> transactionsFiltered = new ArrayList<>();

        for (TransactionRecord transactionRecord : transactionRecords) {
            if (transactionRecord.getDate().isAfter(dateFrom)
                    && transactionRecord.getDate().isBefore(dateTo)
                    && transactionRecord.getMerchant().equals(merchant)
                    && transactionRecord.getType().equals(TransactionType.PAYMENT)
                    && !CsvUtil.getReversals().containsKey(transactionRecord.getID())
            ) {
                transactionsFiltered.add(transactionRecord);
                transactionsSum += transactionRecord.getAmount();
                transactionsNumber++;
            }
        }
        return transactionsFiltered;
    }
}
