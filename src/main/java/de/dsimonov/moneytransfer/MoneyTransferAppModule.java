package de.dsimonov.moneytransfer;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import de.dsimonov.moneytransfer.routing.AccountRouting;
import de.dsimonov.moneytransfer.routing.Routing;
import de.dsimonov.moneytransfer.routing.TransferRouting;
import io.javalin.Javalin;
import org.jetbrains.annotations.NotNull;

public class MoneyTransferAppModule extends AbstractModule {
    private Javalin app;

    public MoneyTransferAppModule(Javalin app) {
        this.app = app;
    }

    @NotNull
    public static MoneyTransferAppModule create() {
        return new MoneyTransferAppModule(Javalin.create());
    }

    @Override
    protected void configure() {
        bind(Javalin.class).toInstance(app);
        Multibinder.newSetBinder(binder(), Routing.class).addBinding().to(AccountRouting.class);
        Multibinder.newSetBinder(binder(), Routing.class).addBinding().to(TransferRouting.class);
    }
}
