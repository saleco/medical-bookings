package com.github.saleco.medicalbookings.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Create Appointment")
public class CreateAppointmentDto {

    @Schema(description = "Appointment's Agenda", required = true)
    @NotNull
    private Long agendaId;

    @Schema(description = "Appointment's Patient", required = true)
    @NotNull
    private Long patientId;


}
