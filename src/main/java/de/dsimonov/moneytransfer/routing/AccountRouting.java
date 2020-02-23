package de.dsimonov.moneytransfer.routing;

import de.dsimonov.moneytransfer.controller.AccountController;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.*;

@Singleton
public class AccountRouting extends Routing<AccountController> {
    private Javalin javalin;

    @Inject
    public AccountRouting(Javalin javalin) {
        this.javalin = javalin;
    }

    @Override
    public void bindRoutes() {
        javalin.routes(() -> {
            path("api/v1/accounts", () -> {
                get(ctx -> getController().getAll(ctx));
                post(ctx -> getController().create(ctx));
                path(":account-id", () -> {
                    get(ctx -> getController().getOne(ctx));
                });
            });
            path("api/internal/v1/accounts", () -> {
                path(":account-id", () -> {
                    delete(ctx -> getController().delete(ctx));
                });
            });
        });
    }
}
