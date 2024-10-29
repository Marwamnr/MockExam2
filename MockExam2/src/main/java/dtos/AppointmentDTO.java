package dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Integer id;
    private String clientName;
    private LocalDate date;
    private LocalTime time;
    private String comment;

    // Constructor to map from an Appointment entity to this DTO
    public AppointmentDTO(String clientName, LocalDate date, LocalTime time, String comment) {
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.comment = comment;
    }
}
