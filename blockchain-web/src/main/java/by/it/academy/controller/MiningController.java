package by.it.academy.controller;

import by.it.academy.pojo.MiningSession;
import by.it.academy.pojo.Wallet;
import by.it.academy.service.MiningSessionService;
import by.it.academy.service.WalletService;
import by.it.academy.support.MinerChoice;
import by.it.academy.support.MiningSessionStatus;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MiningController {

    @Autowired
    WalletService walletService;
    @Autowired
    MiningSessionService miningSessionService;

    private static ArrayList<Wallet> walletsForChoice = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(MiningController.class);

    @RequestMapping(value = "/{userId}/mining-launch", method = RequestMethod.POST)
    public String launchMining(@ModelAttribute MinerChoice minerChoice,
                               ModelMap model, @PathVariable String userId) {
        model.addAttribute("minerChoice", minerChoice.getMiningWalletId());
        log.info("Asking user " + userId + " to choose wallet for mining");
        log.info(minerChoice.toString());
        model.addAttribute("walletId", minerChoice.getMiningWalletId());
        return "redirect:/{userId}/wallet/{walletId}/mining-request";
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

    @RequestMapping(value = "/{userId}/mining-request",
            method = RequestMethod.GET)
    public String miningRequest(ModelAndView modelAndView, @PathVariable String userId) {
        log.info("Mining request analysis: " + userId);
        ArrayList<MiningSession> miningSessionsForUser = miningSessionService.findAllMiningSessionsForUser(userId);
        log.info("Found mining sessions for user: " + miningSessionsForUser);
        for (MiningSession miningSession : miningSessionsForUser) {
            if (miningSession.getMiningSessionStatus().equals(MiningSessionStatus.IN_PROCESS)) {
                return "redirect:/{userId}/mining-sessions";
            }
        }
        walletsForChoice = (ArrayList<Wallet>) walletService.getAllWalletsForUser(userId, WalletStatus.ACTIVE);
        return "redirect:/{userId}/mining-launch";
    }


    @RequestMapping(value = "/{userId}/mining-sessions",
            method = RequestMethod.GET)
    public ModelAndView miningSessionsView(ModelAndView modelAndView, @PathVariable String userId) {
        log.info("Mining sessions view for user: " + userId);
        float sum = 0.0f;
        ArrayList<MiningSession> miningSessionsForUser = miningSessionService.findAllMiningSessionsForUser(userId);
        for (MiningSession miningSession : miningSessionsForUser) {
            sum += (float) Math.round(miningSession.getMinerReward() * 10.0) / 10.0f;
        }
        modelAndView.setViewName("mining-session-all");
        modelAndView.addObject("miningSessionsForUser", miningSessionsForUser);
        modelAndView.addObject("sum", (float) Math.round(sum * 10.0) / 10.0f);
        return modelAndView;
    }
}
