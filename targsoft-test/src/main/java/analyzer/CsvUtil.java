package analyzer;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CsvUtil {

    //key - reversed transaction ID, value - reversing transaction ID
    private static HashMap<String, String> reversals = new HashMap<>();

    public static HashMap<String, String> getReversals() {
        return reversals;
    }

    public static ArrayList<TransactionRecord> readCSVToBean() throws IOException {
        reversals.clear();
        ICsvBeanReader beanReader = null;
        ArrayList<TransactionRecord> transactionRecords = new ArrayList<>();

        try {
            beanReader = new CsvBeanReader(new FileReader(System.getProperty("user.dir") + "\\src\\main\\resources\\Input.csv"),
                    CsvPreference.STANDARD_PREFERENCE);

            final String[] nameMapping = Arrays.stream(TransactionRecord.class.getDeclaredFields())
                    .map(Field::getName)
                    .toArray(String[]::new);

            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            TransactionRecord transactionRecord;

            while ((transactionRecord = beanReader.read(TransactionRecord.class, nameMapping,
                    processors)) != null) {
                transactionRecords.add(transactionRecord);
                if (transactionRecord.getType().equals(TransactionType.REVERSAL)) {
                    reversals.put(transactionRecord.getRelatedTransaction().get(), transactionRecord.getID());
                }
            }

        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
        return transactionRecords;
    }

    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // ID (must be unique)
                new NotNull(), //Date
                new NotNull(), // Amount
                new NotNull(), // Merchant
                new NotNull(), //Type
                new Optional() //Related transaction
        };
        return processors;
    }
}
