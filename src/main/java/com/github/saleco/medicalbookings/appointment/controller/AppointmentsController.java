package com.github.saleco.medicalbookings.appointment.controller;

import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.CreateAppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.manager.AppointmentManager;
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
@RequestMapping("/ap1/v1/appointments")
@Tag(name = "Appointment's API", description = "Resources related to Appointment's information")
@RequiredArgsConstructor
@Slf4j
public class AppointmentsController {

    private final AppointmentManager appointmentManager;

    @Operation(summary = "Schedule Appointment",
      description = "As a patient, I must be able to see the availability of the doctors")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The Appointment was successfully scheduled.",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class)))),
      @ApiResponse(responseCode = "400", description = "Invalid parameter",
        content = @Content(schema = @Schema(implementation = MedicalBookingsResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not found",
        content = @Content(schema = @Schema(implementation = MedicalBookingsResponse.class)))})
    public AppointmentDto scheduleAppointment(
       @Parameter(name = "Create Appointment DTO", required = true) @RequestBody @Valid CreateAppointmentDto createAppointmentDto) {
        log.debug("Validate inputs for Agenda, Doctor and Patient");
        return appointmentManager.createAppointment(createAppointmentDto);
    }

    @Operation(summary = "Search Appointments for the given criterias" ,
      description = "As a doctor, I must be able to see the appointments that I have for a given time period.")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The list of appointments for the given criterias has been returned",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class)))),
      @ApiResponse(responseCode = "400", description = "Invalid parameter",
        content = @Content(schema = @Schema(implementation = MedicalBookingsResponse.class)))})
    public Page<AppointmentDto> searchAppointments(
      @ParameterObject @Valid SearchAppointmentsDto searchAppointmentsDto) {
        return appointmentManager.searchAppointments(searchAppointmentsDto);
    }
}
