package com.github.saleco.medicalbookings.appointment.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.mapper.AppointmentMapper;
import com.github.saleco.medicalbookings.appointment.model.Appointment;
import com.github.saleco.medicalbookings.appointment.repository.AppointmentRepository;
import com.github.saleco.medicalbookings.mapper.DateMapper;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.model.Patient;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    AppointmentMapper appointmentMapper;

    @Mock
    DateMapper dateMapper;

    @InjectMocks
    AppointmentServiceImpl appointmentService;

    @DisplayName("Given AppointmentDto When createAppointment then should return AppointmentDto")
    @Test
    void givenAppointmentDtoWhenCreateAppointmentThenShouldReturnAppointmentDto() {
        Appointment appointment = mock(Appointment.class);
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        given(appointmentMapper.modelToDto(appointment)).willReturn(appointmentDto);
        given(appointmentMapper.dtoToModel(appointmentDto)).willReturn(appointment);
        given(appointmentRepository.save(appointment)).willReturn(appointment);

        AppointmentDto appointmentDtoReturned = appointmentService.createAppointment(appointmentDto);

        assertThat(appointmentDtoReturned).isNotNull();

        then(appointmentRepository).should(times(1)).save(appointment);
        then(appointmentMapper).should(times(1)).modelToDto(appointment);
        then(appointmentMapper).should(times(1)).dtoToModel(appointmentDto);
        then(appointmentRepository).shouldHaveNoMoreInteractions();
        then(appointmentMapper).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given AppointmentDto When appointmentAlreadyExists then should return boolean")
    @Test
    void givenAppointmentDtoWhenAppointmentAlreadyExistsThenShouldReturnBoolean() {
        AppointmentDto appointmentDto = AppointmentDto
          .builder()
          .patient(PatientDto.builder().id(1L).name("John Thompson").build())
          .agenda(AgendaDto.builder().start(OffsetDateTime.now()).end(OffsetDateTime.now().plusHours(1)).build())
          .build();

        Appointment appointment = Appointment
          .builder()
          .patient(Patient.builder().id(1L).name("John Thompsom").build())
          .agenda(Agenda.builder().start(Timestamp.from(Instant.now())).end(Timestamp.from(Instant.now().plusSeconds(3600))).build())
          .build();

        given(appointmentMapper.dtoToModel(appointmentDto)).willReturn(appointment);
        doReturn(true).when(appointmentRepository).existsAppointmentByPatientAndAgenda_StartAndAgenda_End(appointment.getPatient(), appointment.getAgenda().getStart(), appointment.getAgenda().getEnd());

        Boolean returnedValue = appointmentService.appointmentAlreadyExists(appointmentDto);

        assertThat(returnedValue).isTrue();

        then(appointmentRepository).should(times(1)).existsAppointmentByPatientAndAgenda_StartAndAgenda_End(any(Patient.class), any(Timestamp.class), any(Timestamp.class));
        then(appointmentMapper).should(times(1)).dtoToModel(appointmentDto);
        then(appointmentRepository).shouldHaveNoMoreInteractions();
        then(appointmentMapper).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Given SearchAppointmentsDto When searchAppointments then should return Appointments")
    @Test
    void givenSearchCriteriaWhenSearchAppointmentsThenShouldReturnAppointments() {
        SearchAppointmentsDto searchAppointmentsDto = SearchAppointmentsDto
          .builder()
          .doctorId(1L)
          .startingFrom(OffsetDateTime.now())
          .endingAt(OffsetDateTime.now().plusHours(2))
          .build();

        given(appointmentRepository.searchAppointmentsBy(any(), anyLong(), any(), any()))
          .willReturn(new PageImpl<>(Lists.newArrayList(new Appointment())));

        given(appointmentMapper.modelToDto(any(Appointment.class))).willReturn(new AppointmentDto());

        given(dateMapper.asTimestamp(any(OffsetDateTime.class))).willReturn(mock(Timestamp.class));

        Page<AppointmentDto> appointmentDtos = appointmentService.searchAppointments(searchAppointmentsDto);

        assertThat(appointmentDtos).isNotNull();
        assertThat(appointmentDtos.getContent()).isNotEmpty();

        then(appointmentRepository).should(times(1))
          .searchAppointmentsBy(any(PageRequest.class), anyLong(), any(Timestamp.class), any(Timestamp.class));

        then(appointmentMapper).should(times(1)).modelToDto(any(Appointment.class));

        then(dateMapper).should(times(2)).asTimestamp(any(OffsetDateTime.class));

        then(dateMapper).shouldHaveNoMoreInteractions();
        then(appointmentRepository).shouldHaveNoMoreInteractions();
        then(appointmentMapper).shouldHaveNoMoreInteractions();

    }
}