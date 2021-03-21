package com.github.saleco.medicalbookings.agenda.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.dto.UpdateDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.mapper.AgendaMapper;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.agenda.repository.AgendaRepository;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.service.AppointmentService;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.mapper.DoctorMapper;
import com.github.saleco.medicalbookings.doctor.service.DoctorService;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import com.github.saleco.medicalbookings.exception.ValidationException;
import com.github.saleco.medicalbookings.mapper.DateMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.print.Doc;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AgendaMapper agendaMapper;

    @Mock
    private DateMapper dateMapper;

    @Mock
    private DoctorService doctorService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private AgendaServiceImpl agendaService;

    @Captor
    private ArgumentCaptor<String> argumentCaptor;

    @DisplayName("Given AgendaDto When createAgenda then should return AgendaDto")
    @Test
    void givenAgendaDtoWhenCreateAgendaThenShouldReturnAgendaDto() {
        Agenda agenda = mock(Agenda.class);
        AgendaDto agendaDto = mock(AgendaDto.class);
        given(agendaMapper.modelToDto(agenda)).willReturn(agendaDto);
        given(agendaMapper.dtoToModel(agendaDto)).willReturn(agenda);
        given(agendaRepository.save(agenda)).willReturn(agenda);

        AgendaDto agendaDtoReturned = agendaService.createAgenda(agendaDto);

        assertThat(agendaDtoReturned).isNotNull();

        then(agendaRepository).should(times(1)).save(agenda);
        then(agendaMapper).should(times(1)).modelToDto(agenda);
        then(agendaMapper).should(times(1)).dtoToModel(agendaDto);
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaMapper).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given SearchDoctorsAvailabilityDto When getAvailability then should return Availability")
    @Test
    void givenSearchCriteriaWhenGetAvailabilityThenShouldReturnAvailability() {
        SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto = SearchDoctorsAvailabilityDto
          .builder()
          .doctorName("John Doe")
          .startingFrom(OffsetDateTime.now())
          .endingAt(OffsetDateTime.now().plusHours(2))
          .build();

        given(agendaRepository.searchAvailabilityBy(any(), argumentCaptor.capture(), any(), any()))
          .willReturn(new PageImpl<>(Lists.newArrayList(new Agenda())));

        given(agendaMapper.modelToDto(any(Agenda.class))).willReturn(new AgendaDto());

        given(dateMapper.asTimestamp(any(OffsetDateTime.class))).willReturn(mock(Timestamp.class));

        Page<AgendaDto> agendaDtoPage = agendaService.getAvailability(searchDoctorsAvailabilityDto);

        assertThat(argumentCaptor.getValue()).isEqualToIgnoringCase("%John Doe%");
        assertThat(agendaDtoPage).isNotNull();
        assertThat(agendaDtoPage.getContent()).isNotEmpty();

        then(agendaRepository).should(times(1))
          .searchAvailabilityBy(any(PageRequest.class), anyString(), any(Timestamp.class), any(Timestamp.class));

        then(agendaMapper).should(times(1)).modelToDto(any(Agenda.class));

        then(dateMapper).should(times(2)).asTimestamp(any(OffsetDateTime.class));

        then(dateMapper).shouldHaveNoMoreInteractions();
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaMapper).shouldHaveNoMoreInteractions();

    }


    @DisplayName("Given not found id Agenda When findById then should throws NotFoundException")
    @Test
    void givenNotFoundIdAgendaWhenFindByIdThenShouldThrowNotFoundException() {
        given(agendaRepository.findById(1L)).willThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> agendaService.findById(1l));
    }

    @DisplayName("Given Id Agenda When findById then should return Agenda")
    @Test
    void givenIdAgendaWhenFindByIdThenShouldReturnAgenda() {
        Agenda agenda = mock(Agenda.class);
        AgendaDto agendaDto = mock(AgendaDto.class);

        given(agendaRepository.findById(1L)).willReturn(Optional.of(agenda));
        given(agendaMapper.modelToDto(agenda)).willReturn(agendaDto);

        AgendaDto result = agendaService.findById(1L);

        assertThat(result).isNotNull();

        then(agendaRepository).should(times(1)).findById(anyLong());
        then(agendaMapper).should(times(1)).modelToDto(any(Agenda.class));

        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaMapper).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given AgendaDto When updateAgenda then should update Agenda")
    @Test
    void givenAgendaDtoWhenUpdateAgendaThenShouldUpdateAgenda() {
        Agenda agenda = mock(Agenda.class);
        AgendaDto agendaDto = mock(AgendaDto.class);
        given(agendaMapper.dtoToModel(agendaDto)).willReturn(agenda);

        agendaService.updateAgenda(agendaDto);

        then(agendaRepository).should(times(1)).save(agenda);
        then(agendaMapper).should(times(1)).dtoToModel(agendaDto);
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaMapper).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given SearchDoctorsAvailabilityDto When getAvailabilityByDoctorId then should Availability")
    @Test
    void givenSearchCriteriaWhenGetAvailabilityByDoctorIdThenShouldReturnAvailability() {
        SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto = SearchDoctorsAvailabilityDto
          .builder()
          .startingFrom(OffsetDateTime.now())
          .endingAt(OffsetDateTime.now().plusHours(2))
          .build();

        given(agendaRepository.searchAvailabilityBy(any(), anyLong(), any(), any()))
          .willReturn(new PageImpl<>(Lists.newArrayList(new Agenda())));

        given(agendaMapper.modelToDto(any(Agenda.class))).willReturn(new AgendaDto());

        given(dateMapper.asTimestamp(any(OffsetDateTime.class))).willReturn(mock(Timestamp.class));

        Page<AgendaDto> agendaDtoPage = agendaService.getAvailabilityByDoctorId(searchDoctorsAvailabilityDto, 1l);

        assertThat(agendaDtoPage).isNotNull();
        assertThat(agendaDtoPage.getContent()).isNotEmpty();

        then(agendaRepository).should(times(1))
          .searchAvailabilityBy(any(PageRequest.class), anyLong(), any(Timestamp.class), any(Timestamp.class));

        then(agendaMapper).should(times(1)).modelToDto(any(Agenda.class));

        then(dateMapper).should(times(2)).asTimestamp(any(OffsetDateTime.class));

        then(dateMapper).shouldHaveNoMoreInteractions();
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(agendaMapper).shouldHaveNoMoreInteractions();
    }


    @DisplayName("Given not found id Doctor When updateAvailability then should throws NotFoundException")
    @Test
    void givenNotFoundIdDoctorWhenUpdateAvailabilityThenShouldThrowNotFoundException() {
        UpdateDoctorsAvailabilityDto availabilityDto = UpdateDoctorsAvailabilityDto.builder().doctorId(1L).build();
        given(doctorService.findById(availabilityDto.getDoctorId())).willThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> agendaService.updateAvailability(availabilityDto));

        then(doctorService).should(times(1)).findById(anyLong());
        then(doctorService).shouldHaveNoMoreInteractions();
        then(agendaRepository).shouldHaveNoInteractions();
        then(appointmentService).shouldHaveNoInteractions();
    }

    @DisplayName("Given UpdateDoctorsAvailabilityDto When updateAvailability hasAppointmentsInPeriod then should throws ValidationException")
    @Test
    void givenUpdateAvailabilityDtoWhenUpdateAvailabilityHasAppointmentThenShouldThrowValidationException() {
        UpdateDoctorsAvailabilityDto availabilityDto =
          UpdateDoctorsAvailabilityDto
            .builder()
            .doctorId(1L)
            .startingFrom(LocalDateTime.now())
            .endingAt(LocalDateTime.now().plusDays(5))
            .build();

        DoctorDto doctorDto = DoctorDto.builder().id(1l).name("John Doe").build();

        given(doctorService.findById(availabilityDto.getDoctorId())).willReturn(doctorDto);
        given(appointmentService.searchAppointments(any(SearchAppointmentsDto.class)))
          .willReturn(new PageImpl<>(Collections.singletonList(new AppointmentDto())));

        assertThrows(ValidationException.class, () -> agendaService.updateAvailability(availabilityDto));

        then(doctorService).should(times(1)).findById(anyLong());
        then(doctorService).shouldHaveNoMoreInteractions();
        then(appointmentService).should(times(1)).searchAppointments(any(SearchAppointmentsDto.class));
        then(appointmentService).shouldHaveNoMoreInteractions();
        then(agendaRepository).shouldHaveNoInteractions();
    }

    @DisplayName("Given UpdateDoctorsAvailabilityDto When updateAvailability then should remove existing agendas and update availability")
    @Test
    void givenUpdateAvailabilityDtoWhenUpdateAvailabilityThenShouldUpdateAvailability() {
        UpdateDoctorsAvailabilityDto availabilityDto =
          UpdateDoctorsAvailabilityDto
            .builder()
            .doctorId(1L)
            .startingFrom(LocalDateTime.now())
            .endingAt(LocalDateTime.now().plusDays(5))
            .build();

        DoctorDto doctorDto = DoctorDto.builder().id(1l).name("John Doe").build();

        given(doctorService.findById(availabilityDto.getDoctorId())).willReturn(doctorDto);
        given(appointmentService.searchAppointments(any(SearchAppointmentsDto.class)))
          .willReturn(new PageImpl<>(Collections.emptyList()));
        given(agendaRepository.searchAvailabilityBy(any(PageRequest.class), anyLong(), any(), any())).willReturn(Page.empty());

        agendaService.updateAvailability(availabilityDto);

        then(doctorService).should(times(1)).findById(anyLong());
        then(doctorService).shouldHaveNoMoreInteractions();
        then(appointmentService).should(times(1)).searchAppointments(any(SearchAppointmentsDto.class));
        then(appointmentService).shouldHaveNoMoreInteractions();
        then(agendaRepository).should(times(1)).searchAvailabilityBy(any(PageRequest.class), anyLong(), any(), any());
        then(agendaRepository).should(times(1)).deleteAll(any());
        then(agendaRepository).should(times(1)).save(any(Agenda.class));
        then(agendaRepository).shouldHaveNoMoreInteractions();
        then(dateMapper).should(times(4)).asTimestamp(any(OffsetDateTime.class));
        then(dateMapper).shouldHaveNoMoreInteractions();
        then(doctorMapper).should(times(1)).dtoToModel(any(DoctorDto.class));
        then(doctorMapper).shouldHaveNoMoreInteractions();
    }


}