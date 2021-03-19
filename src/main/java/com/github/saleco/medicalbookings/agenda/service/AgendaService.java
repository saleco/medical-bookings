package com.github.saleco.medicalbookings.agenda.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import org.springframework.data.domain.Page;

public interface AgendaService {
    AgendaDto save(AgendaDto agendaDto);

    Page<AgendaDto> getAgendas(SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto);
}
