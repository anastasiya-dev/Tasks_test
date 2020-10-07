package by.it.academy.controller;

import by.it.academy.management.TransactionManagement;
import by.it.academy.pojo.Transaction;
import by.it.academy.support.FilterInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/{userId}/wallet/{walletId}/transaction-all")
public class TransactionViewController {

    @Autowired
    TransactionManagement transactionManagement;

    private static final Logger log = LoggerFactory.getLogger(TransactionViewController.class);

    static ArrayList<Transaction> transactionsForDownload = new ArrayList<>();
    FilterInput savedFilterInput = null;

    @RequestMapping(value = {"", "/{page}"}, method = RequestMethod.GET)
    public ModelAndView viewAllTransactions(
            @PathVariable String userId,
            @PathVariable String walletId,
            @ModelAttribute FilterInput filterInput,
            @PathVariable(required = false, name = "page") String page,
            HttpServletRequest request,
            HttpServletResponse response) {

        transactionsForDownload.clear();
        log.info("Cleared transactions filtered: " + transactionsForDownload);
        log.info("Received filter requirement " + filterInput + " for wallet " + walletId);


        if (page == null) {
            savedFilterInput = filterInput;
        } else {
            filterInput = savedFilterInput;
        }

        ModelAndView modelAndView = new ModelAndView();

        List<Transaction> transactions = transactionManagement.getAllForWallet(walletId, false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ArrayList<Transaction> transactionsFiltered = new ArrayList<>();
        LocalDateTime dateStart;
        LocalDateTime dateEnd;
        float valueMin;
        float valueMax;

        if (filterInput.getDateEnd() == null || filterInput.getDateEnd().equals("")) {
            dateEnd = LocalDateTime.now();
        } else {
            dateEnd = LocalDateTime.parse(filterInput.getDateEnd(), formatter);
        }
        if (filterInput.getDateStart() == null || filterInput.getDateStart().equals("")) {
            dateStart = LocalDateTime.parse("2020-01-01 00:00", formatter);
        } else {
            dateStart = LocalDateTime.parse(filterInput.getDateStart(), formatter);
        }
        if (filterInput.getValueMin() == null || filterInput.getValueMin().equals("")) {
            valueMin = (float) Integer.MIN_VALUE;
        } else {
            valueMin = Float.parseFloat(filterInput.getValueMin());
        }
        if (filterInput.getValueMax() == null || filterInput.getValueMax().equals("")) {
            valueMax = (float) Integer.MAX_VALUE;
        } else {
            valueMax = Float.parseFloat(filterInput.getValueMax());
        }

        float sum = 0.0f;
        for (Transaction transaction : transactions) {

            if (((filterInput.getSender() == null) || (filterInput.getSender().equals("")) || (transaction.getSenderId().contains(filterInput.getSender())))
                    && (((filterInput.getRecipient() == null) || filterInput.getRecipient().equals("")) || (transaction.getRecipientId().contains(filterInput.getRecipient())))
                    && (transaction.getValue() >= valueMin)
                    && (transaction.getValue() <= valueMax)
                    && (transaction.getTransactionDateTime().isAfter(dateStart))
                    && (transaction.getTransactionDateTime().isBefore(dateEnd))
                    && (((filterInput.getOperationType() == null) || filterInput.getOperationType().equals("")) || transaction.getTransactionStatus().name().toLowerCase().contains(filterInput.getOperationType().toLowerCase()))
            ) {
                sum += (float) Math.round(transaction.getValue() * 10.0) / 10.0f;
                transactionsFiltered.add(transaction);
            }
        }
        modelAndView.setViewName("transaction-all");

        transactionsForDownload.addAll(transactionsFiltered);
        log.info("Extracted transactions filtered: " + transactionsForDownload);
        modelAndView.addObject("sum", (float) Math.round(sum * 10.0) / 10.0f);
        pagination(request, response, page, transactionsFiltered);
        return modelAndView;
    }

    @RequestMapping(value = {"", "/{page}"}, method = RequestMethod.POST)
    public ModelAndView filterTransactions(ModelAndView modelAndView,
                                           @PathVariable String userId,
                                           @PathVariable String walletId,
                                           @ModelAttribute FilterInput filterInput) {
        return modelAndView;
    }

    private void pagination(HttpServletRequest request,
                            HttpServletResponse response,
                            String page,
                            ArrayList<Transaction> transactionsFiltered) {
        PagedListHolder<Transaction> transactionList;
        if (page == null) {
            transactionList = new PagedListHolder<>();
            // Setting the source for PagedListHolder
            transactionList.setSource(transactionsFiltered);
            transactionList.setPageSize(5);
            // Setting PagedListHolder instance to session
            request.getSession().setAttribute("transactionList", transactionList);
        } else if (page.equals("prev")) {
            // get the user list from session
            transactionList = (PagedListHolder<Transaction>) request.getSession().getAttribute("transactionList");
            // switch to previous page
            transactionList.previousPage();
        } else if (page.equals("next")) {
            transactionList = (PagedListHolder<Transaction>) request.getSession().getAttribute("transactionList");
            // switch to next page
            transactionList.nextPage();
        } else {
            int pageNum = Integer.parseInt(page);
            transactionList = (PagedListHolder<Transaction>) request.getSession().getAttribute("transactionList");
            // set the current page number
            // page number starts from zero in PagedListHolder that's why subtracting 1
            transactionList.setPage(pageNum - 1);
        }
    }
}
