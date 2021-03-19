package com.github.saleco.medicalbookings.agenda.mapper;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.mapper.DateMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface AgendaMapper {

    AgendaDto modelToDto(Agenda agenda);
    Agenda dtoToModel(AgendaDto agendaDto);

}
