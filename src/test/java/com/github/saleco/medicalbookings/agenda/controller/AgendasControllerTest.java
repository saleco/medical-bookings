package com.github.saleco.medicalbookings.agenda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AgendasController.class)
class AgendasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendaService agendaService;


    @DisplayName("Given Invalid Request when getAgendas then Should return status 4XX.")
    @Test
    void givenInvalidRequestWhenGetAgendasThenShouldReturnStatus4XX() throws Exception {
        mockMvc
          .perform(get("/ap1/v1/agendas/search")
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().is4xxClientError())
          .andExpect(content().string(containsString("We are sorry, something unexpected happened. Please try it again later.")));

        then(agendaService).shouldHaveNoInteractions();
    }

    @DisplayName("Given request without mandatory fields when getAgendas then Should return status 4XX.")
    @Test
    void givenRequestWithoutMandatoryFieldsWhenGetAgendasThenShouldReturnStatus4XX() throws Exception {
        mockMvc
          .perform(get("/ap1/v1/agendas/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new SearchDoctorsAvailabilityDto()))
          )
          .andExpect(status().is4xxClientError())
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
          .perform(get("/ap1/v1/agendas/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(searchDoctorsAvailabilityDto))
          ).andExpect(status().is2xxSuccessful());

        then(agendaService).should(times(1)).getAvailability(any(SearchDoctorsAvailabilityDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
    }
}