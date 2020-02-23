package de.dsimonov.moneytransfer.service;

import de.dsimonov.moneytransfer.model.Account;
import de.dsimonov.moneytransfer.model.Transfer;
import de.dsimonov.moneytransfer.repository.impl.InMemoryTransferRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Objects;

@Singleton
public class TransferService {
    private InMemoryTransferRepository transferRepository;
    private AccountService accountService;

    @Inject
    public TransferService(InMemoryTransferRepository transferRepository, AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    public Transfer createTransfer(Transfer transfer) {
        return transferRepository.create(transfer);
    }

    public Transfer getTransfer(Long transferId) {
        return transferRepository.getOne(transferId);
    }

    public boolean execute(Transfer transfer) {
        Long sourceAccountId = transfer.getSourceAccountId();
        Long destinationAccountId = transfer.getDestinationAccountId();

        if (accountService.getAccount(sourceAccountId) == null) {
            return false;
        }

        if (accountService.getAccount(destinationAccountId) == null) {
            return false;
        }

        BigDecimal amount = transfer.getAmount();

        BigDecimal withdrawalResult = withdraw(sourceAccountId, amount);

        if (Objects.equals(withdrawalResult, BigDecimal.ZERO)) {
            return false;
        }

        deposit(destinationAccountId, amount);

        return true;
    }

    private BigDecimal withdraw(Long accountId, BigDecimal amountToWithdraw) {
        Account account = accountService.getAccount(accountId);
        while (true) {
            BigDecimal current = account.getBalance();

            if (current.compareTo(amountToWithdraw) < 0) {
                return BigDecimal.ZERO;
            }

            if (account.updateBalance(current, current.subtract(amountToWithdraw))) {
                return amountToWithdraw;
            }
        }
    }

    private void deposit(Long accountId, BigDecimal amountToDeposit) {
        Account account = accountService.getAccount(accountId);
        while (true) {
            BigDecimal current = account.getBalance();
            if (account.updateBalance(current, current.add(amountToDeposit)))
                break;
        }
    }

    public void deleteTransfer(Long transferId) {
        transferRepository.delete(transferId);
    }
}
