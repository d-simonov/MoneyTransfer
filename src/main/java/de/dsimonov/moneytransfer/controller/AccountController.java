package de.dsimonov.moneytransfer.controller;

import de.dsimonov.moneytransfer.model.Account;
import de.dsimonov.moneytransfer.service.AccountService;
import io.javalin.http.Context;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class AccountController {

    private AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void getAll(Context context) {
        context.json(accountService.getAllAccounts());
    }

    public void create(Context context) {
        Account account = context.bodyAsClass(Account.class);
        account = accountService.createAccount(account);
        context.status(201);
        context.json(account);
    }

    public void delete(Context context) {
        Long accountId = Long.valueOf(Objects.requireNonNull(context.pathParam("account-id")));
        accountService.deleteAccount(accountId);
    }

    public void getOne(Context context) {
        Long accountId = Long.valueOf(Objects.requireNonNull(context.pathParam("account-id")));
        Account account = accountService.getAccount(accountId);
        if (account == null) {
            context.status(404);
        } else {
            context.json(account);
        }
    }
}
