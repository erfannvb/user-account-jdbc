package com.example.service;

import com.example.entity.Account;
import com.example.repository.account.AccountRepository;
import com.example.repository.account.AccountRepositoryImpl;

public class AccountService {

    private final AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();

    public void saveAccount(Long userId, String accountName) {
        Account account = new Account();
        account.setAccountId(accountIdGenerator());
        account.setUserId(userId);
        account.setAccountName(accountName);
        accountRepository.save(account);
    }

    public void printAccountInfo() {
        Account[] accounts = accountRepository.loadAllUsingArray();
        for (Account account : accounts) {
            System.out.println(account.getAccountName());
        }
    }

    private long accountIdGenerator() {
        return accountRepository.loadId() + 1;
    }

}
