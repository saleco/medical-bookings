package com.github.saleco.medicalbookings.agenda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Doctor's Availability Update")
public class UpdateDoctorsAvailabilityDto {

    @Schema(description = "Doctor's Identification", example = "1")
    @NotNull
    private Long doctorId;

    @Schema(description = "Start from", required = true, format = "date-time", example = "2021-03-25T09:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDateTime startingFrom;

    @Schema(description = "Ending at", required = true, format = "date-time", example = "2021-03-25T14:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDateTime endingAt;


}
