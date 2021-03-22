package com.github.saleco.medicalbookings.agenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.dto.UpdateDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AgendasController.class)
class AgendasControllerTest {

    public static final String AGENDAS_API = "/ap1/v1/agendas";
    public static final String AGENDAS_SEARCH_API = "/ap1/v1/agendas/search";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendaService agendaService;


    @DisplayName("Given Invalid Request when getAgendas then Should return status 400.")
    @Test
    void givenInvalidRequestWhenGetAgendasThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(get(AGENDAS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when getAgendas then Should return status 400.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenGetAgendasThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(get(AGENDAS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(SearchDoctorsAvailabilityDto.builder().build()))
          )
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given correct request when getAgendas then Should return status 2XX.")
    @Test
    void givenCorrectRequestWhenGetAgendasThenStatusShouldBe2XX() throws Exception {
        SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto = SearchDoctorsAvailabilityDto
          .builder()
          .startingFrom(OffsetDateTime.now())
          .endingAt(OffsetDateTime.now().plusHours(5))
          .build();

        mockMvc
          .perform(get(AGENDAS_SEARCH_API)
            .contentType(MediaType.APPLICATION_JSON)
            .param("startingFrom", searchDoctorsAvailabilityDto.getStartingFrom().toString())
            .param("endingAt", searchDoctorsAvailabilityDto.getEndingAt().toString())
          ).andExpect(status().is2xxSuccessful());

        then(agendaService).should(times(1)).getAvailability(any(SearchDoctorsAvailabilityDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Invalid Request when updateDoctorsAvailability then Should return status 400.")
    @Test
    void givenInvalidRequestWhenUpdateDoctorsAvailabilityThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(put(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("We are sorry, something unexpected happened. Please try it again later.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when updateDoctorsAvailability then Should return status 400.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenUpdateDoctorsAvailabilityThenShouldReturnStatus400() throws Exception {
        mockMvc
          .perform(put(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new UpdateDoctorsAvailabilityDto()))
          )
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("There is a validation rule that prevents the request.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request when updateDoctorsAvailability throws NotFoundException then Should return status 404.")
    @Test
    void givenRequesWhenUpdateDoctorsAvailabilityThrowsNotFoundExceptionThenShouldReturnStatus404() throws Exception {
        UpdateDoctorsAvailabilityDto updateDoctorsAvailabilityDto =
          UpdateDoctorsAvailabilityDto
            .builder()
            .doctorId(1L)
            .startingFrom(LocalDateTime.now())
            .endingAt(LocalDateTime.now().plusDays(4))
            .build();

        doThrow(NotFoundException.class).when(agendaService).updateAvailability(updateDoctorsAvailabilityDto);

        mockMvc
          .perform(put(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDoctorsAvailabilityDto))
          )
          .andExpect(status().isNotFound());

        then(agendaService).should(times(1)).updateAvailability(any(UpdateDoctorsAvailabilityDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given correct request when updateDoctorsAvailability then Should return status 200.")
    @Test
    void givenRequestWhenUpdateDoctorsAvailabilityThenShouldReturnStatus200() throws Exception {
        UpdateDoctorsAvailabilityDto updateDoctorsAvailabilityDto =
          UpdateDoctorsAvailabilityDto
            .builder()
            .doctorId(1L)
            .startingFrom(LocalDateTime.now())
            .endingAt(LocalDateTime.now().plusDays(4))
            .build();

        mockMvc
          .perform(put(AGENDAS_API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDoctorsAvailabilityDto))
          )
          .andExpect(status().is2xxSuccessful());

        then(agendaService).should(times(1)).updateAvailability(any(UpdateDoctorsAvailabilityDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
    }
}