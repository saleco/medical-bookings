package com.github.saleco.medicalbookings.agenda.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.dto.UpdateDoctorsAvailabilityDto;
import org.springframework.data.domain.Page;

public interface AgendaService {
    AgendaDto createAgenda(AgendaDto agendaDto);

    Page<AgendaDto> getAvailability(SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto);

    AgendaDto findById(Long id);

    void updateAgenda(AgendaDto agendaDto);

    void updateAvailability(UpdateDoctorsAvailabilityDto updateDoctorsAvailabilityDto);
}
