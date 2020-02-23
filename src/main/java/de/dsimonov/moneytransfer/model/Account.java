package de.dsimonov.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class Account extends AbstractEntity {
    private final String accountNumber;
    private final AtomicReference<BigDecimal> balance;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Account(@JsonProperty("accountNumber") String accountNumber, @JsonProperty("balance") BigDecimal balance,
                   @JsonProperty("id") Long id) {
        this.accountNumber = accountNumber;
        this.balance = new AtomicReference<>(balance);
        this.id = id;
    }

    public Account(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = new AtomicReference<>(balance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance.get();
    }

    public boolean updateBalance(BigDecimal expected, BigDecimal updated) {
        return balance.compareAndSet(expected, updated);
    }
}
