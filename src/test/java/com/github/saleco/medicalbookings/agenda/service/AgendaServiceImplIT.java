package com.github.saleco.medicalbookings.agenda.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgendaServiceImplIT {

    @Autowired
    private AgendaService agendaService;

    @Test
    void save() {
    }

    @Test
    void getAgendas() {
        SearchDoctorsAvailabilityDto searchAvailability = SearchDoctorsAvailabilityDto
          .builder()
          .startingFrom(OffsetDateTime.of(2021, 3, 22, 9, 0, 0, 0, ZoneOffset.UTC))
          .endingAt(OffsetDateTime.of(2021, 3, 22, 10, 0, 0, 0, ZoneOffset.UTC))
          .build();

        Page<AgendaDto> agendaDtos = agendaService.getAgendas(searchAvailability);
        assertNotNull(agendaDtos);
        assertFalse(agendaDtos.isEmpty());
    }
}