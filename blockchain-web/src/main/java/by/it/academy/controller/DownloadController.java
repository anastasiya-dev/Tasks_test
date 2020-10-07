package by.it.academy.controller;

import by.it.academy.ApplicationConfiguration;
import by.it.academy.pojo.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
public class DownloadController {

    private static final Logger log = LoggerFactory.getLogger(DownloadController.class);

    @RequestMapping(value = "/{userId}/wallet/{walletId}/download")
    public void download(ModelAndView modelAndView,
                         @PathVariable String userId,
                         @PathVariable String walletId,
                         HttpServletResponse response) throws IOException {

        DateTimeFormatter FORMATTER = ApplicationConfiguration.FORMATTER;
        String formatted = ZonedDateTime.now().format(FORMATTER);

        String csvFileName = formatted + "_transactions.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"TransactionId", "TransactionStatus", "SenderId",
                "RecipientId", "Value", "TransactionDateTime", "BlockId"};

        csvWriter.writeHeader(header);

        for (Transaction transaction : TransactionViewController.transactionsForDownload) {
            csvWriter.write(transaction, header);
        }
        csvWriter.close();
        log.info("Saved " + TransactionViewController.transactionsForDownload);
        log.info("Location file name: " + csvFileName);
        TransactionViewController.transactionsForDownload.clear();
    }
}
