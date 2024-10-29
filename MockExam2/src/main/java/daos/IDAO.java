package daos;

import dtos.DoctorDTO;

import java.time.LocalDate;
import java.util.List;
import Enum.Speciality;

public interface IDAO {

    List<DoctorDTO> readAll();
    DoctorDTO read(int id);
    List<DoctorDTO> doctorBySpeciality(Speciality speciality);
    List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to);
    DoctorDTO create(DoctorDTO doctor);
    DoctorDTO update(int id, DoctorDTO doctor);


}
