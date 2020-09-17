package by.it.academy.controller;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.TransactionService;
import by.it.academy.service.UserService;
import by.it.academy.service.UtxoService;
import by.it.academy.service.WalletService;
import by.it.academy.support.FilterInput;
import by.it.academy.support.PrivateKeyInput;
import by.it.academy.support.TransactionStart;
import by.it.academy.support.TransactionStatus;
import by.it.academy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    UtxoService utxoService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{userId}/wallet/{walletId}/create-transaction",
            method = RequestMethod.GET)
    public ModelAndView transactionPage(ModelAndView modelAndView,
                                        @PathVariable String userId,
                                        @PathVariable String walletId) {
        modelAndView.setViewName("create-transaction");
        return modelAndView;
    }


    @RequestMapping(value = "/{userId}/wallet/{walletId}/create-transaction",
            method = RequestMethod.POST)
    public String createTransaction(ModelAndView modelAndView,
                                    @PathVariable String userId,
                                    @PathVariable String walletId,
                                    @ModelAttribute TransactionStart transactionStart,
                                    RedirectAttributes redirectAttributes
    ) {

        Wallet walletSender = walletService.findWalletById(walletId);
        Wallet walletRecipient = walletService.findWalletById(transactionStart.getRecipient());
        System.out.println("wallet recipient found: ");
        System.out.println(walletRecipient);
        Float value = Float.valueOf(transactionStart.getValue());

        Transaction transaction
                = transactionService.createTransaction(
                walletSender.getWalletId(),
                transactionStart.getRecipient(),
                value
        );

        transactionService.createNewTransaction(transaction);
        System.out.println(transaction);
        String transactionId = transaction.getTransactionId();
        redirectAttributes.addAttribute("transactionId", transactionId);
        modelAndView.setViewName("create-transaction");
        return "redirect:/{userId}/wallet/{walletId}/transaction/{transactionId}/sign-transaction";
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/transaction/{transactionId}/sign-transaction",
            method = RequestMethod.POST)
    public String signTransaction(ModelAndView modelAndView,
                                  @PathVariable String userId,
                                  @PathVariable String walletId,
                                  @PathVariable String transactionId,
                                  RedirectAttributes redirectAttributes,
                                  @ModelAttribute PrivateKeyInput privateKeyInput) {

        modelAndView.setViewName("sign-transaction");
        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        Transaction transaction = transactionService.findTransactionById(transactionId);
        if (!privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            System.out.println("#Unauthorized");
            return "redirect:/home";
        } else if (walletService.getBalance(walletSender) < transaction.getValue()) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return "redirect:/home";
        } else {
            transactionService.generateSignature(transaction, walletSender.privateKey);
            transactionService.createNewTransaction(transaction);
            Transaction signedTr = transactionService.findTransactionById(transaction.transactionId);
            walletService.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTr);
            return "redirect:/{userId}/user-cabinet";
        }
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/transaction/{transactionId}/sign-transaction",
            method = RequestMethod.GET)
    public ModelAndView signTransactionPage(ModelAndView modelAndView,
                                            @PathVariable String userId,
                                            @PathVariable String walletId,
                                            @PathVariable String transactionId,
                                            RedirectAttributes redirectAttributes,
                                            @ModelAttribute PrivateKeyInput privateKeyInput) {

        Transaction transaction = transactionService.findTransactionById(transactionId);
        modelAndView.addObject("transaction", transaction);
//        modelAndView.addObject("recipient", transaction.getRecipient());
//        modelAndView.addObject("value", transaction.getValue());

        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();
        System.out.println(StringUtil.getStringFromKey(privateKey));

        modelAndView.setViewName("sign-transaction");
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}/transaction-all", method = RequestMethod.GET)
    public ModelAndView viewAllTransactions(ModelAndView modelAndView,
                                            @PathVariable String userId,
                                            @PathVariable String walletId,
                                            @ModelAttribute FilterInput filterInput) {

        List<Transaction> transactions = transactionService.getAllForWallet(walletId);
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
            if (!transaction.getTransactionStatus().equals(TransactionStatus.CREATED)
                    && ((filterInput.getSender() == null) || (filterInput.getSender().equals("")) || (transaction.getSenderId().contains(filterInput.getSender())))
                    && (((filterInput.getRecipient() == null) || filterInput.getRecipient().equals("")) || (transaction.getRecipientId().contains(filterInput.getRecipient())))
                    && (transaction.getValue() >= valueMin)
                    && (transaction.getValue() <= valueMax)
                    && (transaction.getTransactionDateTime().isAfter(dateStart))
                    && (transaction.getTransactionDateTime().isBefore(dateEnd))
                    && (((filterInput.getStatus() == null) || filterInput.getStatus().equals("")) || transaction.getTransactionStatus().toString().toLowerCase().contains(filterInput.getStatus().toLowerCase()))
            ) {
                sum += (float) Math.round(transaction.value * 10.0) / 10.0f;
                transactionsFiltered.add(transaction);
            }
        }
        modelAndView.setViewName("transaction-all");
        modelAndView.addObject("transactions", transactionsFiltered);
        if (!filterInput.equals(new FilterInput())) {
            modelAndView.addObject("message", "Transactions filtered");
        }
        modelAndView.addObject("sum", sum);
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet/{walletId}/transaction-all", method = RequestMethod.POST)
    public ModelAndView filterTransactions(ModelAndView modelAndView,
                                           @PathVariable String userId,
                                           @PathVariable String walletId,
                                           @ModelAttribute FilterInput filterInput) {

        List<Transaction> transactions = transactionService.getAllForWallet(walletId);
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
            if (!transaction.getTransactionStatus().equals(TransactionStatus.CREATED)
                    && ((filterInput.getSender() == null) || (filterInput.getSender().equals("")) || (transaction.getSenderId().contains(filterInput.getSender())))
                    && (((filterInput.getRecipient() == null) || filterInput.getRecipient().equals("")) || (transaction.getRecipientId().contains(filterInput.getRecipient())))
                    && (transaction.getValue() >= valueMin)
                    && (transaction.getValue() <= valueMax)
                    && (transaction.getTransactionDateTime().isAfter(dateStart))
                    && (transaction.getTransactionDateTime().isBefore(dateEnd))
                    && (((filterInput.getStatus() == null) || filterInput.getStatus().equals("")) || transaction.getTransactionStatus().toString().toLowerCase().contains(filterInput.getStatus().toLowerCase()))
            ) {
                sum += (float) Math.round(transaction.value * 10.0) / 10.0f;
                transactionsFiltered.add(transaction);
            }
        }
        modelAndView.setViewName("transaction-all");
        modelAndView.addObject("transactions", transactionsFiltered);
        if (!filterInput.equals(new FilterInput())) {
            modelAndView.addObject("message", "Transactions filtered");
        }
        modelAndView.addObject("sum", sum);
        return modelAndView;
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/unconfirmed",
            method = RequestMethod.GET)
    public ModelAndView viewUnconfirmed(ModelAndView modelAndView,
                                        @PathVariable String userId,
                                        @PathVariable String walletId,
                                        @ModelAttribute PrivateKeyInput privateKeyInput
//            ,
//                                        @PathVariable String transactionId,
//                                        RedirectAttributes redirectAttributes,
//                                        @ModelAttribute PrivateKeyInput privateKeyInput
    ) {
        List<Transaction> transactions = transactionService.getAllForWallet(walletId);
        ArrayList<Transaction> unconfirmedTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionStatus().equals(TransactionStatus.CREATED)
                    && transaction.getSenderId().equals(walletId)) {
                unconfirmedTransactions.add(transaction);
            }
        }
        modelAndView.setViewName("view-unconfirmed");
        modelAndView.addObject("transactions", unconfirmedTransactions);
//        signAll(modelAndView, userId, walletId, privateKeyInput);

        return modelAndView;
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/unconfirmed/sign-all",
            method = RequestMethod.POST)
    public String signAll(ModelAndView modelAndView,
                          @PathVariable String userId,
                          @PathVariable String walletId,
//                                  @PathVariable String transactionId,
//                                  RedirectAttributes redirectAttributes,
                          @ModelAttribute PrivateKeyInput privateKeyInput) {

        modelAndView.setViewName("view-unconfirmed");
        Wallet walletSender = walletService.findWalletById(walletId);
        PrivateKey privateKey = walletSender.getPrivateKey();

        List<Transaction> transactions = transactionService.getAllForWallet(walletId);
        ArrayList<Transaction> unconfirmedTransactions = new ArrayList<>();
        float sum = 0.0f;
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionStatus().equals(TransactionStatus.CREATED)
                    && transaction.getSenderId().equals(walletId)) {
                unconfirmedTransactions.add(transaction);
                sum += transaction.getValue();
            }
        }
        modelAndView.setViewName("view-unconfirmed");
        modelAndView.addObject("transactions", unconfirmedTransactions);
//        Transaction transaction = transactionService.findTransactionById(transactionId);
        if (!privateKeyInput.getPrivateKeyString()
                .equals(StringUtil.getStringFromKey(privateKey))) {
            System.out.println("#Unauthorized");
            return "redirect:/home";
        } else if (walletService.getBalance(walletSender) < sum) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return "redirect:/home";
        } else {

            for (Transaction transaction : unconfirmedTransactions) {
                transactionService.generateSignature(transaction, walletSender.privateKey);
                transactionService.createNewTransaction(transaction);
                Transaction signedTr = transactionService.findTransactionById(transaction.transactionId);
                walletService.sendFunds(walletSender, transaction.getRecipient(), transaction.getValue(), signedTr);
            }
            return "redirect:/{userId}/user-cabinet";
        }
    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/unconfirmed/delete-all",
            method = RequestMethod.GET)
    public String deleteAll(ModelAndView modelAndView,
                            @PathVariable String userId,
                            @PathVariable String walletId
//            ,
//                                  @PathVariable String transactionId,
//                                  RedirectAttributes redirectAttributes,
//                          @ModelAttribute PrivateKeyInput privateKeyInput
    ) {

        modelAndView.setViewName("delete-all");
        Wallet walletSender = walletService.findWalletById(walletId);
//        Transaction transaction = transactionService.findTransactionById(transactionId);

        List<Transaction> transactions = transactionService.getAllForWallet(walletId);
        ArrayList<Transaction> unconfirmedTransactions = new ArrayList<>();
        float sum = 0.0f;
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionStatus().equals(TransactionStatus.CREATED)
                    && transaction.getSenderId().equals(walletId)) {
                unconfirmedTransactions.add(transaction);
                sum += transaction.getValue();
            }
        }

        for (Transaction transaction : unconfirmedTransactions) {
            transactionService.deleteTransaction(transaction);
        }
        return "redirect:/{userId}/user-cabinet";

    }

    @RequestMapping(
            value = "/{userId}/wallet/{walletId}/transaction/{transactionId}/delete-transaction",
            method = RequestMethod.GET)
    public String deleteTransaction(ModelAndView modelAndView,
                                    @PathVariable String userId,
                                    @PathVariable String walletId,
                                    @PathVariable String transactionId
    ) {
        Transaction transaction = transactionService.findTransactionById(transactionId);
        modelAndView.setViewName("view-unconfirmed");
        transactionService.deleteTransaction(transaction);
        return "redirect:/{userId}/wallet/{walletId}/unconfirmed";
    }
}