package com.github.saleco.medicalbookings.agenda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Agenda")
public class AgendaDto {

    @Schema(description = "Agenda's Identification", required = true, example = "1")
    private Long id;

    @Schema(description = "Agenda's Doctor", required = true)
    private DoctorDto doctor;

    @Schema(description = "Agenda's Start Time", required = true, example = "2020-01-01 10:00")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime start;

    @Schema(description = "Agenda's End Time", required = true, example = "2020-01-01 11:00")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime end;

    @Schema(description = "Agenda's availability", defaultValue = "true", example = "true")
    private boolean available;

}
