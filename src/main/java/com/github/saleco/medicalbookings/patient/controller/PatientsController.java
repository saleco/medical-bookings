package com.github.saleco.medicalbookings.patient.controller;

import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ap1/v1/patients")
@Tag(name = "Patients's API", description = "Resources related to Patient's information")
@RequiredArgsConstructor
@Slf4j
public class PatientsController {

    private final PatientService patientService;

    @Operation(summary = "Retrieve the list of patients")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The list of patients has been returned",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = PatientDto.class))))})
    public Page<PatientDto> getPatients(@Parameter(description = "Page number. Starting from 0")
                                        @RequestParam(defaultValue = "0") int page,
                                        @Parameter(description = "Page size. By default 20")
                                        @RequestParam(defaultValue = "20") int size) {
        return patientService.getPatients(page, size);
    }
}

