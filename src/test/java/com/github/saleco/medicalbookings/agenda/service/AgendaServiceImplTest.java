package com.github.saleco.medicalbookings.agenda.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.mapper.AgendaMapper;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.agenda.repository.AgendaRepository;
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

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
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

    @InjectMocks
    private AgendaServiceImpl agendaService;

    @Captor
    private ArgumentCaptor<String> argumentCaptor;

    @DisplayName("Given AgendaDto When save then should return AgendaDto")
    @Test
    void givenAgendaDtoWhenSaveThenShouldReturnAgendaDto() {
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

    }

    @DisplayName("Given SearchDoctorsAvailabilityDto When getAgendas then should return Availability")
    @Test
    void getAvailability() {
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
}