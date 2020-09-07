package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    @Value("#{walletDao}")
    BaseDao walletDao;

    public boolean createNewWallet(Wallet wallet) {
        walletDao.create(wallet);
        return true;
    }

    public List<Wallet> getAll(String userId) {
        List<Wallet> all = walletDao.findAll("");
        List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : all) {
            if (wallet.getUser().getUserId().equals(userId)) {
                wallets.add(wallet);
            }
        }
        return wallets;
    }

    public Wallet findWalletById(String id) {
        return (Wallet) walletDao.findById(id);
    }
}
