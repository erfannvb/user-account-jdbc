package com.example.repository.account;

import com.example.entity.Account;

import java.util.List;

public interface AccountRepository {
    void save(Account account);

    long saveAndReturnAccountId(Account account);

    void saveAll(List<Account> accountList);

    void update(Account account);

    void deleteById(Long accountId);

    Account loadById(Long accountId);

    List<Account> loadAll();

    Account[] loadAllUsingArray();

    int getNumberOfAccounts();

    long loadId();
}
