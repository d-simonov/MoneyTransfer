package de.dsimonov.moneytransfer.service;

import de.dsimonov.moneytransfer.model.Account;
import de.dsimonov.moneytransfer.repository.impl.InMemoryAccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class AccountService {
    private InMemoryAccountRepository accountRepository;

    @Inject
    public AccountService(InMemoryAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Collection<Account> getAllAccounts() {
        return accountRepository.getAll();
    }

    public Account getAccount(Long accountId) {
        return accountRepository.getOne(accountId);
    }

    public Account createAccount(Account account) {
        return accountRepository.create(account);
    }

    public void deleteAccount(Long accountId) {
        accountRepository.delete(accountId);
    }
}
