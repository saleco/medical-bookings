package com.github.saleco.medicalbookings.agenda.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.mapper.AgendaMapper;
import com.github.saleco.medicalbookings.agenda.repository.AgendaRepository;
import com.github.saleco.medicalbookings.mapper.DateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository agendaRepository;
    private final AgendaMapper agendaMapper;
    private final DateMapper dateMapper;

    @Override
    public AgendaDto save(AgendaDto agendaDto) {
        return agendaMapper
          .modelToDto(agendaRepository.save(agendaMapper.dtoToModel(agendaDto)));
    }

    @Override
    public Page<AgendaDto> getAgendas(SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto) {
        String likeCriteria = "%name%"
          .replaceAll("name", searchDoctorsAvailabilityDto.getDoctorName() != null ? searchDoctorsAvailabilityDto.getDoctorName(): "");


        return agendaRepository.searchAvailabilityBy(
          PageRequest.of(searchDoctorsAvailabilityDto.getPageNumber(), searchDoctorsAvailabilityDto.getPageSize(), Sort.Direction.ASC, "doctor", "start"),
          likeCriteria,
          dateMapper.asTimestamp(searchDoctorsAvailabilityDto.getStartingFrom()),
          dateMapper.asTimestamp(searchDoctorsAvailabilityDto.getEndingAt()))
          .map(agendaMapper::modelToDto);
    }
}
