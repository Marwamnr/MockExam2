package security.controllers;

import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;
import security.enums.Role;

import java.util.Set;

public class AccessController implements IAccessController {

    SecurityController securityController = SecurityController.getInstance();

    public void accessHandler(Context ctx) {

        // Hvis der ikke er specificeret roller på endpointen kan alle tilgå ruten
        if (ctx.routeRoles().isEmpty() || ctx.routeRoles().contains(Role.ANYONE)){
            return;
        }

        // Tjek om brugeren er autentificeret
        try {
            securityController.authenticate().handle(ctx);
        } catch (UnauthorizedResponse e) {
            throw new UnauthorizedResponse(e.getMessage());
        } catch (Exception e) {
            throw new UnauthorizedResponse("Du skal logge ind, makker! Eller også er din token ugyldig.");
        }

        // Tjek om brugeren har de nødvendige roller til at få adgang til ruten
        UserDTO user = ctx.attribute("user");
        Set<RouteRole> allowedRoles = ctx.routeRoles(); // roller tilladt for den aktuelle rute
        if (!securityController.authorize(user, allowedRoles)) {
            throw new UnauthorizedResponse("Ikke autoriseret med roller: " + user.getRoles() + ". Nødvendige roller er: " + allowedRoles);
        }
    }
}
