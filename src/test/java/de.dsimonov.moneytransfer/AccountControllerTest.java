package de.dsimonov.moneytransfer;

import de.dsimonov.moneytransfer.model.Account;
import io.javalin.plugin.json.JavalinJson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountControllerTest extends AbstractControllerTest {

    public static final String ACCOUNT_NUMBER = "DE1234";

    @Test
    public void postAccountAndGetIt() {
        Account account = new Account(ACCOUNT_NUMBER, BigDecimal.ONE);

        //creating account
        HttpResponse response = post(BASE_URL + "/api/v1/accounts", account);
        assertThat(response.getStatus()).isEqualTo(201);
        Account accountReturned = JavalinJson.fromJson(response.getBody().toString(), Account.class);

        //getting one account
        HttpResponse response1 = Unirest.get(BASE_URL + "/api/v1/accounts/" + accountReturned.getId()).asString();
        assertThat(response1.getStatus()).isEqualTo(200);
        Object body = response1.getBody();
        assertThat(body).isEqualTo(JavalinJson.toJson(accountReturned));

        //getting list of accounts
        HttpResponse response2 = Unirest.get(BASE_URL + "/api/v1/accounts/").asString();
        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getBody()).isEqualTo(JavalinJson.toJson(Collections.singletonList(accountReturned)));
        deleteAccount(accountReturned.getId());
    }

    @Test
    public void getEmptyListOfAccounts() {
        HttpResponse response = Unirest.get(BASE_URL + "/api/v1/accounts").asString();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(JavalinJson.toJson(new ArrayList<>()));
    }

    @Test
    public void getNonexistentAccount() {
        HttpResponse response1 = Unirest.get(BASE_URL + "/api/v1/accounts/" + 100).asString();
        assertThat(response1.getStatus()).isEqualTo(404);
    }
}