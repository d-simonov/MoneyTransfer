package de.dsimonov.moneytransfer.routing;

import de.dsimonov.moneytransfer.controller.TransferController;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.*;

@Singleton
public class TransferRouting extends Routing<TransferController> {

    private Javalin javalin;

    @Inject
    public TransferRouting(Javalin javalin) {
        this.javalin = javalin;
    }

    @Override
    public void bindRoutes() {
        javalin.routes(() -> {
            path("api/v1/transfers", () -> {
                post(ctx -> getController().create(ctx));
                path(":transfer-id", () -> {
                    put(ctx -> getController().execute(ctx));
                });
            });
            path("api/internal/v1/transfers", () -> {
                path(":transfer-id", () -> {
                    delete(ctx -> getController().delete(ctx));
                });
            });
        });
    }
}
