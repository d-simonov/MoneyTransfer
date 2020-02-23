package de.dsimonov.moneytransfer.repository.impl;

import de.dsimonov.moneytransfer.model.Account;
import de.dsimonov.moneytransfer.repository.AbstractInMemoryRepository;

import javax.inject.Singleton;

@Singleton
public class InMemoryAccountRepository extends AbstractInMemoryRepository<Account> {
}
