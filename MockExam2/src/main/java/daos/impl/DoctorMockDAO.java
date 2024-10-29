package daos;

import dtos.DoctorDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import Enum.Speciality;

public class DoctorMockDAO implements IDAO {
    private static final List<DoctorDTO> doctors = new ArrayList<>();

    static {
        doctors.add(new DoctorDTO("John Doe", LocalDate.of(1980, 1, 1), 2005, "City Clinic", Speciality.SURGERY));
        doctors.add(new DoctorDTO("Jane Smith", LocalDate.of(1985, 5, 15), 2010, "Health Center", Speciality.PSYCHIATRY));
        doctors.add(new DoctorDTO("Emily Johnson", LocalDate.of(1978, 7, 20), 2000, "Downtown Medical", Speciality.PEDIATRICS));
    }


    public List<DoctorDTO> readAll() {
        return new ArrayList<>(doctors);
    }

    public DoctorDTO read(int id) {
        return doctors.stream()
                .filter(doctor -> doctor.getId() == id)
                .findFirst()
                .orElse(null);
    }



    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) {
        return doctors.stream()
                .filter(doctor -> doctor.getSpeciality().equals(speciality))
                .collect(Collectors.toList());
    }


    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) {
        return doctors.stream()
                .filter(doctor -> doctor.getDateOfBirth().isAfter(from) || doctor.getDateOfBirth().isEqual(from) && doctor.getDateOfBirth().isBefore(to) || doctor.getDateOfBirth().isEqual(to))
                .collect(Collectors.toList());
    }

    public DoctorDTO create(DoctorDTO doctor) {
        doctors.add(doctor);
        return doctor;
    }

    public DoctorDTO update(int id, DoctorDTO updatedDoctor) {
        Optional<DoctorDTO> existingDoctorOpt = doctors.stream()
                .filter(doctor -> doctor.getId() == id)
                .findFirst();

        if (existingDoctorOpt.isPresent()) {
            DoctorDTO existingDoctor = existingDoctorOpt.get();
            existingDoctor.setName(updatedDoctor.getName());
            existingDoctor.setDateOfBirth(updatedDoctor.getDateOfBirth());
            existingDoctor.setYearOfGraduation(updatedDoctor.getYearOfGraduation());
            existingDoctor.setNameOfClinic(updatedDoctor.getNameOfClinic());
            existingDoctor.setSpeciality(updatedDoctor.getSpeciality());
            return existingDoctor;
        }
        return null;
    }
}


