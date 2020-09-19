package by.it.academy.controller;

import by.it.academy.management.TransactionManagement;
import by.it.academy.pojo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class DownloadController {

    @Autowired
    TransactionManagement transactionManagement;

    @RequestMapping(value = "/{userId}/wallet/{walletId}/download")
    public void download(ModelAndView modelAndView,
                         @PathVariable String userId,
                         @PathVariable String walletId,
                         HttpServletResponse response) throws IOException {

        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss");
        String formatted = ZonedDateTime.now().format(FORMATTER);

        String csvFileName = formatted + "_transactions.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        List<Transaction> transactions = transactionManagement.getAllForWallet(walletId, false);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"TransactionId", "TransactionStatus", "SenderId",
                "RecipientId", "Value", "TransactionDateTime", "BlockId"};

        csvWriter.writeHeader(header);

        for (Transaction transaction : transactions) {
            csvWriter.write(transaction, header);
        }
        csvWriter.close();
    }
}
