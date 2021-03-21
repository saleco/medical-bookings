package com.github.saleco.medicalbookings.agenda.controller;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.dto.UpdateDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import com.github.saleco.medicalbookings.utils.MedicalBookingsResponse;
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
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ap1/v1/agendas")
@Tag(name = "Agendas's API", description = "Resources related to Agendas's information")
@RequiredArgsConstructor
@Slf4j
public class AgendasController {

    private final AgendaService agendaService;

    @Operation(summary = "Get Doctors Availability",
    description = "As a patient, I must be able to schedule an appointment for myself.")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The list of doctors Availability has been returned",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendaDto.class)))),
      @ApiResponse(responseCode = "400", description = "Invalid parameter",
      content = @Content(schema = @Schema(implementation = MedicalBookingsResponse.class)))})
    public Page<AgendaDto> getAgendas(
      @ParameterObject @Valid SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto) {
        return agendaService.getAvailability(searchDoctorsAvailabilityDto);
    }

    @Operation(summary = "Update Doctors Availability",
    description = "As a doctor, I can set my self as unavailable for a specific time period. blocking any " +
      "patients from scheduling an appointment for that period.")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The doctors availability was successfully updated."),
      @ApiResponse(responseCode = "400", description = "Invalid parameter",
        content = @Content(schema = @Schema(implementation = MedicalBookingsResponse.class))),
    @ApiResponse(responseCode = "404", description = "Not found",
      content = @Content(schema = @Schema(implementation = MedicalBookingsResponse.class)))})
    public void updateDoctorsAvailability(
      @Parameter(name = "Update Doctors Availability DTO", required = true) @RequestBody @Valid UpdateDoctorsAvailabilityDto updateDoctorsAvailabilityDto) {
        agendaService.updateAvailability(updateDoctorsAvailabilityDto);
    }
}


