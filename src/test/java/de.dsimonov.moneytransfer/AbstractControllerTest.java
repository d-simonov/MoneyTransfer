package de.dsimonov.moneytransfer;

import io.javalin.plugin.json.JavalinJson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractControllerTest {

    protected static int PORT;
    protected final String BASE_URL = "http://localhost:" + PORT;

    @BeforeAll
    static void beforeAll() {
        generatePort();
        JavalinApp.start(PORT);
    }

    @AfterAll
    static void afterAll() {
        JavalinApp.stop();
    }

    protected HttpResponse<String> post(String baseUrl, Object any) {
        return Unirest.post(baseUrl)
                .body(JavalinJson.toJson(any))
                .asString();
    }

    protected void deleteAccount(Long accountId) {
        Unirest.delete(BASE_URL + "/api/internal/v1/accounts/" + accountId).asString();
    }

    public static void generatePort(){
        PORT = ThreadLocalRandom.current().nextInt(9000, 10000);
    }
}
