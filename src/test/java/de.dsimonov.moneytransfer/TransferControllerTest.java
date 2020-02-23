package de.dsimonov.moneytransfer;

import de.dsimonov.moneytransfer.model.Account;
import de.dsimonov.moneytransfer.model.Transfer;
import io.javalin.plugin.json.JavalinJson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferControllerTest extends AbstractControllerTest {
    private final static Account ACCOUNT_1 = new Account("DE1234", BigDecimal.TEN);
    private final static Account ACCOUNT_2 = new Account("DE4321", BigDecimal.TEN);

    @Test
    public void postTransferAndExecuteIt() {
        //creating two accounts
        HttpResponse response1 = post(BASE_URL + "/api/v1/accounts", ACCOUNT_1);
        assertThat(response1.getStatus()).isEqualTo(201);
        Long sourceAccountId = JavalinJson.fromJson(response1.getBody().toString(), Account.class).getId();

        HttpResponse response2 = post(BASE_URL + "/api/v1/accounts", ACCOUNT_2);
        assertThat(response2.getStatus()).isEqualTo(201);
        Long targetAccountId = JavalinJson.fromJson(response2.getBody().toString(), Account.class).getId();

        //creating transfer
        Transfer transfer = new Transfer(sourceAccountId, targetAccountId, BigDecimal.TEN);

        HttpResponse response3 = post(BASE_URL + "/api/v1/transfers/", transfer);
        assertThat(response2.getStatus()).isEqualTo(201);
        Long transferId = JavalinJson.fromJson(response3.getBody().toString(), Transfer.class).getId();

        //executing transfer
        HttpResponse response4 = Unirest.put(BASE_URL + "/api/v1/transfers/" + transferId).asString();
        Boolean result = JavalinJson.fromJson(response4.getBody().toString(), Boolean.class);
        assertThat(result).isEqualTo(true);

        //checking transfer status and balances
        HttpResponse response5 = Unirest.get(BASE_URL + "/api/v1/accounts/" + sourceAccountId).asString();
        Account accountSource = JavalinJson.fromJson(response5.getBody().toString(), Account.class);

        HttpResponse response6 = Unirest.get(BASE_URL + "/api/v1/accounts/" + targetAccountId).asString();
        Account accountTarget = JavalinJson.fromJson(response6.getBody().toString(), Account.class);

        assertThat(accountSource.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(accountTarget.getBalance()).isEqualTo(new BigDecimal(20));

        deleteAccount(sourceAccountId);
        deleteAccount(targetAccountId);
        deleteTransfer(transferId);
    }

    @Test
    public void transferDoesNotExist() {
        HttpResponse response = Unirest.put(BASE_URL + "/api/v1/transfers/" + 1).asString();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    protected void deleteTransfer(Long transferId) {
        Unirest.delete(BASE_URL + "/api/internal/v1/transfers/" + transferId).asString();
    }
}
