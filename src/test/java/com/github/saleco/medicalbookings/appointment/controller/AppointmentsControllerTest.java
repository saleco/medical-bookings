package com.github.saleco.medicalbookings.appointment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.dto.UpdateDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.appointment.controller.AppointmentsController;
import com.github.saleco.medicalbookings.appointment.dto.CreateAppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.manager.AppointmentManager;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentsController.class)
class AppointmentsControllerTest {

    public static final String APPOINTMENTS_API = "/ap1/v1/appointments";
    public static final String APPOINTMENTS_SEARCH_API = "/ap1/v1/appointments/search";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentManager appointmentManager;


    @DisplayName("Given Invalid Request when scheduleAppointment then Should return status 400.")
    @Test
    void givenInvalidRequestWhenScheduleAppointmentThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(post(APPOINTMENTS_API)
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("We are sorry, something unexpected happened. Please try it again later.")));

        then(appointmentManager).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when scheduleAppointment then Should return status 400.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenScheduleAppointmentThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(post(APPOINTMENTS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(CreateAppointmentDto.builder().build()))
          )
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(appointmentManager).shouldHaveNoInteractions();
    }

    @DisplayName("Given correct request when scheduleAppointment then Should return status 200.")
    @Test
    void givenCorrectRequestWhenScheduleAppointmentThenStatusShouldBe200() throws Exception {
        CreateAppointmentDto createAppointmentDto = CreateAppointmentDto
          .builder()
          .patientId(1L)
          .agendaId(1L)
          .build();

        mockMvc
          .perform(post(APPOINTMENTS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createAppointmentDto))
          ).andExpect(status().is2xxSuccessful());

        then(appointmentManager).should(times(1)).createAppointment(any(CreateAppointmentDto.class));
        then(appointmentManager).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Invalid Request when searchAppointment then Should return status 400.")
    @Test
    void givenInvalidRequestWhenSearchAppointmentThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(get(APPOINTMENTS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(appointmentManager).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when searchAppointment then Should return status 400.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenSearchAppointmentThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(get(APPOINTMENTS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(SearchAppointmentsDto.builder().build()))
          )
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(appointmentManager).shouldHaveNoInteractions();
    }

    @DisplayName("Given correct request when searchAppointment then Should return status 200.")
    @Test
    void givenCorrectRequestWhenSearchAppointmentThenStatusShouldBe200() throws Exception {
        SearchAppointmentsDto searchAppointmentsDto = SearchAppointmentsDto
          .builder()
          .doctorId(1L)
          .startingFrom(OffsetDateTime.now())
          .endingAt(OffsetDateTime.now().plusHours(5))
          .build();

        mockMvc
          .perform(get(APPOINTMENTS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON)
            .param("startingFrom", searchAppointmentsDto.getStartingFrom().toString())
            .param("endingAt", searchAppointmentsDto.getEndingAt().toString())
            .param("doctorId", searchAppointmentsDto.getDoctorId().toString())
            .content(objectMapper.writeValueAsString(searchAppointmentsDto))
          ).andExpect(status().is2xxSuccessful());

        then(appointmentManager).should(times(1)).searchAppointments(any(SearchAppointmentsDto.class));
        then(appointmentManager).shouldHaveNoMoreInteractions();
    }
}