package de.dsimonov.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer extends AbstractEntity {

    private final Long sourceAccountId;
    private final Long destinationAccountId;
    private final BigDecimal amount;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Transfer(@JsonProperty("sourceAccountId") Long sourceAccountId,
                    @JsonProperty("destinationAccountId") Long destinationAccountId,
                    @JsonProperty("amount") BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
