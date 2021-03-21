package com.github.saleco.medicalbookings.appointment.manager;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.CreateAppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.service.AppointmentService;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import com.github.saleco.medicalbookings.exception.ValidationException;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AppointmentManagerTest {

    @Mock
    AppointmentService appointmentService;

    @Mock
    AgendaService agendaService;

    @Mock
    PatientService patientService;

    @InjectMocks
    AppointmentManager appointmentManager;

    @DisplayName("Given CreateAppointmentDto with invalid agenda id when createAppointment then should throw NotFoundException")
    @Test
    void givenCreateAppointmentDtoWhenCreateAppointmentWithInvalidAgendaIdThenShouldThrowNotFoundException() {
        given(agendaService.findById(anyLong())).willThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
          () -> appointmentManager.createAppointment(CreateAppointmentDto.builder().agendaId(1l).build()));

        then(agendaService).should(times(1)).findById(anyLong());
        then(agendaService).shouldHaveNoMoreInteractions();
        then(patientService).shouldHaveNoInteractions();
        then(appointmentService).shouldHaveNoInteractions();
    }

    @DisplayName("Given CreateAppointmentDto with invalid patient id when createAppointment then should throw NotFoundException")
    @Test
    void givenCreateAppointmentDtoWhenCreateAppointmentWithInvalidPatientIdThenShouldThrowNotFoundException() {
        given(agendaService.findById(anyLong())).willReturn(mock(AgendaDto.class));
        given(patientService.findById(anyLong())).willThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
          () -> appointmentManager.createAppointment(CreateAppointmentDto.builder().agendaId(1L).patientId(1L).build()));

        then(agendaService).should(times(1)).findById(anyLong());
        then(agendaService).shouldHaveNoMoreInteractions();
        then(patientService).should(times(1)).findById(anyLong());
        then(patientService).shouldHaveNoMoreInteractions();
        then(appointmentService).shouldHaveNoInteractions();
    }

    @DisplayName("Given CreateAppointmentDto when createAppointment with agenda unavailable then should throw ValidationException")
    @Test
    void givenCreateAppointmentDtoWhenCreateAppointmentWithAgendaUnavailableThenShouldThrowValidationException() {
        AgendaDto agendaDto = mock(AgendaDto.class);
        given(agendaDto.isAvailable()).willReturn(false);
        given(agendaService.findById(anyLong())).willReturn(agendaDto);
        given(patientService.findById(anyLong())).willReturn(mock(PatientDto.class));

        assertThrows(ValidationException.class,
          () -> appointmentManager.createAppointment(CreateAppointmentDto.builder().agendaId(1L).patientId(1L).build()));

        then(agendaService).should(times(1)).findById(anyLong());
        then(agendaService).shouldHaveNoMoreInteractions();
        then(patientService).should(times(1)).findById(anyLong());
        then(patientService).shouldHaveNoMoreInteractions();
        then(appointmentService).shouldHaveNoInteractions();

    }

    @DisplayName("Given CreateAppointmentDto when createAppointment with existing appointment for patient then should throw ValidationException")
    @Test
    void givenCreateAppointmentDtoWhenCreateAppointmentWithExistingAppointmentForPatientThenShouldThrowValidationException() {
        AgendaDto agendaDto = mock(AgendaDto.class);
        given(agendaDto.isAvailable()).willReturn(true);
        given(agendaService.findById(anyLong())).willReturn(agendaDto);
        given(patientService.findById(anyLong())).willReturn(mock(PatientDto.class));
        given(appointmentService.appointmentAlreadyExists(any(AppointmentDto.class))).willReturn(true);

        assertThrows(ValidationException.class,
          () -> appointmentManager.createAppointment(CreateAppointmentDto.builder().agendaId(1L).patientId(1L).build()));

        then(agendaService).should(times(1)).findById(anyLong());
        then(agendaService).shouldHaveNoMoreInteractions();
        then(patientService).should(times(1)).findById(anyLong());
        then(patientService).shouldHaveNoMoreInteractions();
        then(appointmentService).should(times(1)).appointmentAlreadyExists(any(AppointmentDto.class));
        then(appointmentService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given CreateAppointmentDto when createAppointment then should updateAgenda and createAppointment")
    @Test
    void givenCreateAppointmentDtoWhenCreateAppointmentThenShouldUpdateAgendaAndCreateAppointment() {
        AgendaDto agendaDto = mock(AgendaDto.class);
        given(agendaDto.isAvailable()).willReturn(true);
        given(agendaService.findById(anyLong())).willReturn(agendaDto);
        given(patientService.findById(anyLong())).willReturn(mock(PatientDto.class));
        given(appointmentService.appointmentAlreadyExists(any(AppointmentDto.class))).willReturn(false);

        appointmentManager.createAppointment(CreateAppointmentDto.builder().agendaId(1L).patientId(1L).build());

        then(agendaService).should(times(1)).findById(anyLong());
        then(agendaService).should(times(1)).updateAgenda(any(AgendaDto.class));
        then(agendaService).shouldHaveNoMoreInteractions();
        then(patientService).should(times(1)).findById(anyLong());
        then(patientService).shouldHaveNoMoreInteractions();
        then(appointmentService).should(times(1)).appointmentAlreadyExists(any(AppointmentDto.class));
        then(appointmentService).should(times(1)).createAppointment(any(AppointmentDto.class));
        then(appointmentService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given SearchAppointmentDto when searchAppointments Then should Return Appointments")
    @Test
    void givenSearchAppointmentsDtoWhenSearchAppointmentsThenShouldReturnAppointments() {
        AppointmentDto appointmentDto = AppointmentDto.builder().build();
        given(appointmentService.searchAppointments(any(SearchAppointmentsDto.class))).willReturn(mock(Page.class));
        Page<AppointmentDto> appointmentDtos = appointmentManager.searchAppointments(mock(SearchAppointmentsDto.class));

        assertThat(appointmentDtos).isNotNull();
        then(appointmentService).should(times(1)).searchAppointments(any(SearchAppointmentsDto.class));
        then(appointmentService).shouldHaveNoMoreInteractions();
    }
}