package by.it.academy.controller;

import by.it.academy.management.WalletManagement;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.WalletService;
import by.it.academy.support.MinerChoice;
import by.it.academy.support.WalletStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class WalletViewController {

    @Autowired
    WalletService walletService;
    @Autowired
    WalletManagement walletManagement;
    private static ArrayList<Wallet> walletsForChoice = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(WalletViewController.class);

    @RequestMapping(value = "/{userId}/create-wallet", method = RequestMethod.GET)
    public ModelAndView createWallet(ModelAndView modelAndView,
                                     @PathVariable String userId) {
        Wallet wallet = walletService.createWallet(userId);
        walletService.saveWallet(wallet);
        log.info("User " + userId + " created new wallet");
        log.info(String.valueOf(wallet));
        modelAndView.addObject("wallet", wallet);
        modelAndView.setViewName("create-wallet");
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/wallet-all", method = RequestMethod.GET)
    public ModelAndView viewAllTheWallets(ModelAndView modelAndView,
                                          @PathVariable String userId) {
        List<Wallet> wallets = walletService.getAllWalletsForUser(userId, WalletStatus.ACTIVE);
        walletsForChoice = (ArrayList<Wallet>) wallets;
        Float sum = 0.0f;
        for (Wallet wallet : wallets) {
            wallet.setBalance(walletManagement.getBalance(wallet));
            sum += wallet.getBalance();
        }

        wallets.sort(Comparator.comparingInt(w -> (int) (w.getBalance() * (-10.0))));
        log.info("Showing all the wallets of the user " + userId);
        modelAndView.setViewName("wallet-all");
        modelAndView.addObject("wallets", wallets);
        modelAndView.addObject("sum", sum);
        return modelAndView;
    }

    @RequestMapping(value = "/{userId}/mining-launch", method = RequestMethod.POST)
    public String launchMining(@ModelAttribute MinerChoice minerChoice,
                               ModelMap model, @PathVariable String userId) {
        model.addAttribute("minerChoice", minerChoice.getMiningWalletId());
        log.info("Asking user " + userId + " to choose wallet for mining");
        log.info(minerChoice.toString());
        model.addAttribute("walletId", minerChoice.getMiningWalletId());
        return "redirect:/{userId}/wallet/{walletId}/mining";
    }

    @ModelAttribute("walletList")
    public Map<String, String> getWalletList() {
        log.info("Forming drop-down list of options");
        Map<String, String> walletList = new HashMap<>();
        for (Wallet wallet : walletsForChoice) {
            walletList.put(wallet.getWalletId(), wallet.getWalletId());
        }
        return walletList;
    }

    @RequestMapping(value = "/{userId}/mining-launch",
            method = RequestMethod.GET)
    public ModelAndView miningLaunchPage(@PathVariable String userId) {
        log.info("Viewing options for mining for user: " + userId);
        MinerChoice minerChoice = new MinerChoice();
        ModelAndView modelAndView = new ModelAndView("minerChoice", "command", minerChoice);
        modelAndView.setViewName("mining-launch");
        return modelAndView;
    }
}
