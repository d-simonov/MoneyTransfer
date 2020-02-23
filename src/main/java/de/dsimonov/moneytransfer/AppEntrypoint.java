package de.dsimonov.moneytransfer;

import com.google.inject.Inject;
import de.dsimonov.moneytransfer.routing.Routing;
import io.javalin.Javalin;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Set;

@Singleton
public class AppEntrypoint {
    private Javalin app;

    @Inject(optional = true)
    private Set<Routing> routes = Collections.emptySet();

    @Inject
    public AppEntrypoint(Javalin app) {
        this.app = app;
    }

    public void start(int port) {
        bindRoutes();
        app.start(port);
    }

    public void stop() {
        app.stop();
    }

    private void bindRoutes() {
        routes.forEach(Routing::bindRoutes);
    }
}
