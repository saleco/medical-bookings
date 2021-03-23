package com.github.saleco.medicalbookings.doctor.controller;

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
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ap1/v1/doctors")
@Tag(name = "Doctor's API", description = "Resources related to Doctor's information")
@RequiredArgsConstructor
@Slf4j
public class DoctorsController {

    private final DoctorService doctorService;

    @Operation(summary = "Retrieve the list of doctors")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The list of doctors has been returned",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class))))})
    public Page<DoctorDto> getDoctors(@Parameter(description = "Page number. Starting from 0")
                                      @RequestParam(defaultValue = "0") int page,
                                      @Parameter(description = "Page size. By default 20")
                                      @RequestParam(defaultValue = "20") int size) {
        return doctorService.getDoctors(page, size);
    }
}

