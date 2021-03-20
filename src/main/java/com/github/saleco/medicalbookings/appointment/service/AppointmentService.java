package com.github.saleco.medicalbookings.appointment.service;

import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import org.springframework.data.domain.Page;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto appointmentDto);
    boolean appointmentAlreadyExists(AppointmentDto appointmentDto);
    Page<AppointmentDto> searchAppointments(SearchAppointmentsDto searchAppointmentsDto);
}
