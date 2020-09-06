package by.it.academy;

import by.it.academy.pojo.Wallet;
import by.it.academy.util.WalletUtil;

public class Main {
    public static void main(String[] args) {
        Wallet wallet = WalletUtil.createWallet();
        System.out.println(wallet);
    }

}
