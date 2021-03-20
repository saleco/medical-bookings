package com.github.saleco.medicalbookings.appointment.mapper;

import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.model.Appointment;
import com.github.saleco.medicalbookings.mapper.DateMapper;
import org.mapstruct.Mapper;


@Mapper(uses = {DateMapper.class})
public interface AppointmentMapper {
    AppointmentDto modelToDto(Appointment appointment);
    Appointment dtoToModel(AppointmentDto appointmentDto);
}
