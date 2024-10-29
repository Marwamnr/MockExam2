package routes;

import controllers.impl.DoctorMockController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class DoctorRoute {
    private final DoctorMockController doctorMockController = new DoctorMockController();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/doctors", () -> {
                get("/", doctorMockController::readAll); // Matches "/api/doctors"
                post("/", doctorMockController::create); // Matches "/api/doctors"
                path("/{id}", () -> {
                    get("/", doctorMockController::read); // Matches "/api/doctors/{id}"
                    put("/", doctorMockController::update); // Matches "/api/doctors/{id}"
                });
                get("/speciality/{speciality}", doctorMockController::doctorBySpeciality); // Matches "/api/doctors/speciality/{speciality}"
                get("/birthdate/range", doctorMockController::doctorByBirthdateRange); // Matches "/api/doctors/birthdate/range"
            });
        };
    }
}
