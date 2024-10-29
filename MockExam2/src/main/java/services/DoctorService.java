package services;

import dtos.DoctorDTO;
import entities.Doctor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class DoctorService {

    private final EntityManagerFactory entityManagerFactory;

    public DoctorService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public String updateDoctor(int doctorId, DoctorDTO doctorDTO) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Find lægen ud fra ID
            Doctor doctor = em.find(Doctor.class, doctorId);

            // Hvis lægen ikke findes, returneres en fejlmeddelelse
            if (doctor == null) {
                return "Doctor not found";
            }

            // Opdater kun de tilladte felter
            doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
            doctor.setYearOfGraduation(doctorDTO.getYearOfGraduation());
            doctor.setNameOfClinic(doctorDTO.getNameOfClinic());
            doctor.setSpeciality(doctorDTO.getSpeciality());

            // Gem opdateringerne
            em.merge(doctor);
            transaction.commit();

            return "Doctor updated successfully";
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
            return "Error updating doctor";
        } finally {
            em.close();
        }
    }
}
