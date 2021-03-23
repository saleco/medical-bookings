package com.github.saleco.medicalbookings.patient.controller;

import com.github.saleco.medicalbookings.patient.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = PatientsController.class)
class PatientsControllerTest {

    public static final String PATIENTS_API = "/ap1/v1/patients";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @DisplayName("Given no Params  when getPatients then Should use default values and  return status 200.")
    @Test
    void givenNoParamsWhenGetPatientsThenShouldUseDefaultValuesAndReturnStatus200() throws Exception {
        mockMvc
          .perform(get(PATIENTS_API))
          .andExpect(status().is2xxSuccessful());

        then(patientService).should(times(1)).getPatients(0, 20);
        then(patientService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given Page and Size when getPatients then Should return status 200.")
    @Test
    void givenPageAndSizeWhenGetPatientsThenShouldReturnStatus200() throws Exception {
        mockMvc
          .perform(get(PATIENTS_API)
            .param("page", "0")
            .param("size", "20"))
          .andExpect(status().is2xxSuccessful());

        then(patientService).should(times(1)).getPatients(0, 20);
        then(patientService).shouldHaveNoMoreInteractions();
    }
}