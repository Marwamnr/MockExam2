package controllers.impl;

import daos.DoctorMockDAO;
import dtos.DoctorDTO;
import exceptions.ApiException;
import io.javalin.http.Context;
import exceptions.Message;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import Enum.Speciality;

public class DoctorMockController {

    private final DoctorMockDAO doctorDAO;

    public DoctorMockController() {
        this.doctorDAO = new DoctorMockDAO();
    }

    public void readAll(Context ctx) {
        List<DoctorDTO> doctors = doctorDAO.readAll();
        ctx.status(200).json(doctors);
    }

    public void create(Context ctx) {
        DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
        DoctorDTO createdDoctor = doctorDAO.create(jsonRequest);
        ctx.status(201).json(createdDoctor);
    }

    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        DoctorDTO doctor = doctorDAO.read(id);
        if (doctor != null) {
            ctx.status(200).json(doctor);
        } else {
            ctx.status(404).json(new Message(404, "Doctor not found"));
        }
    }

    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        DoctorDTO updatedDoctor = ctx.bodyAsClass(DoctorDTO.class);
        DoctorDTO doctor = doctorDAO.update(id, updatedDoctor);
        if (doctor != null) {
            ctx.status(200).json(doctor);
        } else {
            ctx.status(404).json(new Message(404, "Doctor not found"));
        }
    }

    public void doctorBySpeciality(Context ctx) {
        Speciality speciality = Speciality.valueOf(ctx.pathParam("speciality"));
        List<DoctorDTO> doctors = doctorDAO.doctorBySpeciality(speciality);
        ctx.status(200).json(doctors);
    }


    public void doctorByBirthdateRange(Context ctx) {
        try {

            LocalDate from = LocalDate.parse(ctx.queryParam("from"));
            LocalDate to = LocalDate.parse(ctx.queryParam("to"));


            List<DoctorDTO> doctors = doctorDAO.doctorByBirthdateRange(from, to);


            if (!doctors.isEmpty()) {
                ctx.status(200).json(doctors);
            } else {

                throw new ApiException(404, "No doctors found in the specified range" + from + to);
            }
        } catch (DateTimeParseException e) {
            ctx.status(400).json(new Message(400, "Invalid date format"));
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (ApiException e) {
            ctx.status(e.getStatusCode()).json(new Message(e.getStatusCode(), e.getMessage()));
            System.out.println("ApiException occurred: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).json(new Message(500, "An error occurred while processing the request."));
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



