package com.github.saleco.medicalbookings.appointment.dto;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Appointment")
public class AppointmentDto {

    @Schema(description = "Appointment's Identification", required = true, example = "1")
    private Long id;

    @Schema(description = "Appointment's Agenda", required = true)
    private AgendaDto agenda;

    @Schema(description = "Appointment's Patient", required = true)
    private PatientDto patient;


}
