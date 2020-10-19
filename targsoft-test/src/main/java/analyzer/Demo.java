package analyzer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Demo {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter beginning date: ");
        String dateFromInput = scanner.nextLine();
        System.out.println("Enter ending date: ");
        LocalDateTime dateFrom = LocalDateTime.parse(dateFromInput, TransactionAnalyzer.FORMATTER);
        String dateToInput = scanner.nextLine();
        System.out.println("Enter merchant: ");
        LocalDateTime dateTo = LocalDateTime.parse(dateToInput, TransactionAnalyzer.FORMATTER);
        String merchant = scanner.nextLine();

        ArrayList<TransactionRecord> transactionRecords = CsvUtil.readCSVToBean();
        TransactionAnalyzer.transactionsFilter(dateFrom, dateTo, merchant, transactionRecords);
        System.out.println("Number of transactions = " + TransactionAnalyzer.getTransactionsNumber());
        System.out.println("Average Transaction Value = " + TransactionAnalyzer.getSum() / TransactionAnalyzer.getTransactionsNumber());
    }
}
