package routes;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

public class Routes {
    private final DoctorRoute doctorRoute = new DoctorRoute();

    public EndpointGroup getRoutes() {
        return doctorRoute.getRoutes(); // Delegate to DoctorRoute
    }

    public static void setupRoutes(Javalin app) {
        Routes routes = new Routes();
        routes.getRoutes(); // Register the routes with the app
    }
}
