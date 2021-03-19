package com.github.saleco.medicalbookings.patient.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Patient")
public class PatientDto {

    @Schema(description = "Patient's Identification", required = true, example = "1")
    private Long id;

    @Schema(description = "Patient's Name", required = true, example = "Marie Clark")
    private String name;

}
