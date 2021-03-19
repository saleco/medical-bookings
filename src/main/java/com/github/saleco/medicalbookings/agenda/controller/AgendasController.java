package com.github.saleco.medicalbookings.agenda.controller;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.service.DoctorService;
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

@RestController
@RequestMapping("/ap1/v1/agendas")
@Tag(name = "Agendas's API", description = "Resources related to Agendas's information")
@RequiredArgsConstructor
@Slf4j
public class AgendasController {

    private final AgendaService agendaService;

    @Operation(summary = "Get Doctors Availability")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The list of doctors Availability has been returned",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendaDto.class))))})
    public Page<AgendaDto> getAgendas(
      @ParameterObject SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto) {
        return agendaService.getAgendas(searchDoctorsAvailabilityDto);
    }
}


