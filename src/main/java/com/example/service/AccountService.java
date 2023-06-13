package com.example.service;

import com.example.entity.Account;
import com.example.repository.account.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void saveAccount(Long userId, String accountName) {
        Account account = new Account();
        account.setAccountId(accountIdGenerator());
        account.setUserId(userId);
        account.setAccountName(accountName);
        accountRepository.save(account);
    }

    private long accountIdGenerator() {
        return accountRepository.loadId() + 1;
    }

}
