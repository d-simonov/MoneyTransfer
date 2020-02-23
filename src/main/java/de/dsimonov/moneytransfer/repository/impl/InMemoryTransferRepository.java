package de.dsimonov.moneytransfer.repository.impl;

import de.dsimonov.moneytransfer.model.Transfer;
import de.dsimonov.moneytransfer.repository.AbstractInMemoryRepository;

import javax.inject.Singleton;

@Singleton
public class InMemoryTransferRepository extends AbstractInMemoryRepository<Transfer> {
}
