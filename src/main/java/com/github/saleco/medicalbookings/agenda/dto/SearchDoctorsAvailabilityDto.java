package com.github.saleco.medicalbookings.agenda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.saleco.medicalbookings.dto.PageableDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Doctor's Availability Search")
public class SearchDoctorsAvailabilityDto extends PageableDto {

    @Schema(description = "Doctor's Name", example = "John")
    private String doctorName;

    @Schema(description = "Start from", required = true, format = "date-time", example = "2021-03-25T09:00:00Z")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startingFrom;

    @Schema(description = "Ending at", required = true, format = "date-time", example = "2021-03-25T14:00:00Z")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endingAt;
}
