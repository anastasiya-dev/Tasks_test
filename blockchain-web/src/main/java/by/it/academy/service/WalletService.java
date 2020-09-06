package by.it.academy.service;

import by.it.academy.pojo.Wallet;
import by.it.academy.repository.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    @Value("#{walletDao}")
    BaseDao walletDao;

    public boolean createNewWallet(Wallet wallet) {
        walletDao.create(wallet);
        return true;
    }
}
