package de.dsimonov.moneytransfer;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class JavalinApp {

    public static void main(String[] args) {
        start(9000);
    }

    public static void start(int port) {
        Injector injector = Guice.createInjector(MoneyTransferAppModule.create());
        injector.getInstance(AppEntrypoint.class).start(port);
    }

    public static void stop() {
        Injector injector = Guice.createInjector(MoneyTransferAppModule.create());
        injector.getInstance(AppEntrypoint.class).stop();
    }
}
