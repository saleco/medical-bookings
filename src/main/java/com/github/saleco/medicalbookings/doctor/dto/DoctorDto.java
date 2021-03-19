package com.github.saleco.medicalbookings.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Doctor")
public class DoctorDto {

    @Schema(description = "Doctor's Identification", required = true, example = "1")
    private Long id;

    @Schema(description = "Doctor's Name", required = true, example = "Steve Doe")
    private String name;

}
