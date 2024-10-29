package daos.impl;

import daos.IDAO;
import dtos.DoctorDTO;
import entities.Doctor;
import Enum.Speciality;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO implements IDAO {
    private final EntityManagerFactory emf;

    // Constructor to initialize the EntityManagerFactory
    public DoctorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<DoctorDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d", Doctor.class);
            List<Doctor> doctors = query.getResultList();
            List<DoctorDTO> doctorDTOs = new ArrayList<>();
            for (Doctor doctor : doctors) {
                doctorDTOs.add(new DoctorDTO(
                        doctor.getId(),
                        doctor.getName(),
                        doctor.getDateOfBirth(),
                        doctor.getYearOfGraduation(),
                        doctor.getNameOfClinic(),
                        doctor.getSpeciality()
                ));
            }
            return doctorDTOs;
        } finally {
            em.close();
        }
    }

    @Override
    public DoctorDTO read(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor != null) {
                return new DoctorDTO(
                        doctor.getId(),
                        doctor.getName(),
                        doctor.getDateOfBirth(),
                        doctor.getYearOfGraduation(),
                        doctor.getNameOfClinic(),
                        doctor.getSpeciality()
                );
            } else {
                return null; // Doctor not found
            }
        } finally {
            em.close();
        }
    }

    @Override
    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d WHERE d.dateOfBirth BETWEEN :from AND :to", Doctor.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            List<Doctor> doctors = query.getResultList();
            List<DoctorDTO> doctorDTOs = new ArrayList<>();
            for (Doctor doctor : doctors) {
                doctorDTOs.add(new DoctorDTO(
                        doctor.getId(),
                        doctor.getName(),
                        doctor.getDateOfBirth(),
                        doctor.getYearOfGraduation(),
                        doctor.getNameOfClinic(),
                        doctor.getSpeciality()
                ));
            }
            return doctorDTOs;
        } finally {
            em.close();
        }
    }

    @Override
    public DoctorDTO create(DoctorDTO doctorDTO) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Doctor doctor = new Doctor(
                    doctorDTO.getName(),
                    doctorDTO.getDateOfBirth(),
                    doctorDTO.getYearOfGraduation(),
                    doctorDTO.getNameOfClinic(),
                    doctorDTO.getSpeciality()
            );
            em.persist(doctor);
            em.getTransaction().commit();
            return new DoctorDTO(
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getDateOfBirth(),
                    doctor.getYearOfGraduation(),
                    doctor.getNameOfClinic(),
                    doctor.getSpeciality()
            );
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e; // Handle exception as needed
        } finally {
            em.close();
        }
    }

    @Override
    public DoctorDTO update(int id, DoctorDTO doctorDTO) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor != null) {
                // Update only the required fields
                doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
                doctor.setYearOfGraduation(doctorDTO.getYearOfGraduation());
                doctor.setNameOfClinic(doctorDTO.getNameOfClinic());
                doctor.setSpeciality(doctorDTO.getSpeciality());
                em.merge(doctor);
                em.getTransaction().commit();
                return new DoctorDTO(
                        doctor.getId(),
                        doctor.getName(),
                        doctor.getDateOfBirth(),
                        doctor.getYearOfGraduation(),
                        doctor.getNameOfClinic(),
                        doctor.getSpeciality()
                );
            } else {
                return null; // Doctor not found
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e; // Handle exception as needed
        } finally {
            em.close();
        }
    }

    @Override
    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d WHERE d.speciality = :speciality", Doctor.class);
            query.setParameter("speciality", speciality);
            List<Doctor> doctors = query.getResultList();
            List<DoctorDTO> doctorDTOs = new ArrayList<>();
            for (Doctor doctor : doctors) {
                doctorDTOs.add(new DoctorDTO(
                        doctor.getId(),
                        doctor.getName(),
                        doctor.getDateOfBirth(),
                        doctor.getYearOfGraduation(),
                        doctor.getNameOfClinic(),
                        doctor.getSpeciality()
                ));
            }
            return doctorDTOs;
        } finally {
            em.close();
        }
    }
}
