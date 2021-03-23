package com.github.saleco.medicalbookings.doctor.controller;

import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.doctor.service.DoctorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = DoctorsController.class)
class DoctorsControllerTest {

    public static final String DOCTORS_API = "/ap1/v1/doctors";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @DisplayName("Given no Params  when getDoctors then Should use default values and  return status 200.")
    @Test
    void givenNoParamsWhenGetDoctorsThenShouldUseDefaultValuesAndReturnStatus200() throws Exception {
        mockMvc
          .perform(get(DOCTORS_API))
          .andExpect(status().is2xxSuccessful());

        then(doctorService).should(times(1)).getDoctors(0, 20);
        then(doctorService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Page and Size when getDoctors then Should return status 200.")
    @Test
    void givenPageAndSizeWhenGetDoctorsThenShouldReturnStatus200() throws Exception {
        mockMvc
          .perform(get(DOCTORS_API)
            .param("page", "0")
            .param("size", "20"))
          .andExpect(status().is2xxSuccessful());

        then(doctorService).should(times(1)).getDoctors(0, 20);
        then(doctorService).shouldHaveNoMoreInteractions();
    }
}