package config;

import entities.Doctor;
import entities.Appointment;
import Enum.Speciality;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.time.LocalTime;

public class Populator {
    private final EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void populate() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Opret Doctor 1
            Doctor doctor1 = new Doctor("Dr. Alice Smith", LocalDate.of(1975, 4, 12), 2000, "City Health Clinic", Speciality.FAMILY_MEDICINE);
            em.persist(doctor1);

            // Opret Appointments for Doctor 1
            Appointment appointment1 = new Appointment("John Smith", LocalDate.of(2023, 11, 24), LocalTime.of(9, 45), "First visit");
            appointment1.setDoctor(doctor1); // Sæt doctor
            em.persist(appointment1);

            Appointment appointment2 = new Appointment("Alice Johnson", LocalDate.of(2023, 11, 27), LocalTime.of(10, 30), "Follow up");
            appointment2.setDoctor(doctor1); // Sæt doctor
            em.persist(appointment2);

            // Opret Doctor 2
            Doctor doctor2 = new Doctor("Dr. Bob Johnson", LocalDate.of(1980, 8, 5), 2005, "Downtown Medical Center", Speciality.SURGERY);
            em.persist(doctor2);

            // Opret Appointments for Doctor 2
            Appointment appointment3 = new Appointment("Bob Anderson", LocalDate.of(2023, 12, 12), LocalTime.of(14, 0), "General check");
            appointment3.setDoctor(doctor2); // Sæt doctor
            em.persist(appointment3);

            Appointment appointment4 = new Appointment("Emily White", LocalDate.of(2023, 12, 15), LocalTime.of(11, 0), "Consultation");
            appointment4.setDoctor(doctor2); // Sæt doctor
            em.persist(appointment4);

            // Opret Doctor 3
            Doctor doctor3 = new Doctor("Dr. Clara Lee", LocalDate.of(1983, 7, 22), 2008, "Green Valley Hospital", Speciality.PEDIATRICS);
            em.persist(doctor3);
            // Opret Appointments for Doctor 3
            Appointment appointment5 = new Appointment("David Martinez", LocalDate.of(2023, 12, 18), LocalTime.of(15, 30), "Routine checkup");
            appointment5.setDoctor(doctor3);
            em.persist(appointment5);

            Appointment appointment6 = new Appointment("Clara Lee", LocalDate.of(2023, 12, 20), LocalTime.of(8, 45), "Vaccine shot");
            appointment6.setDoctor(doctor3);
            em.persist(appointment6);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Håndter eventuelle undtagelser
        } finally {
            em.close();
        }
    }
}
