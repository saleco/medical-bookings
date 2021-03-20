package com.github.saleco.medicalbookings.appointment.service;

import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.mapper.AppointmentMapper;
import com.github.saleco.medicalbookings.appointment.model.Appointment;
import com.github.saleco.medicalbookings.appointment.repository.AppointmentRepository;
import com.github.saleco.medicalbookings.mapper.DateMapper;
import com.github.saleco.medicalbookings.service.AbstractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl extends AbstractService implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DateMapper dateMapper;

    @Override
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        log.debug("Creating appointment: {}", appointmentDto);
        return appointmentMapper
          .modelToDto(appointmentRepository.save(appointmentMapper.dtoToModel(appointmentDto)));
    }

    @Override
    public boolean appointmentAlreadyExists(AppointmentDto appointmentDto) {
        log.debug("Searching existing appointment for patient: {} from {} to {}", appointmentDto.getPatient(), appointmentDto.getAgenda().getStart(), appointmentDto.getAgenda().getEnd());
        Appointment appointment = appointmentMapper.dtoToModel(appointmentDto);
        return appointmentRepository.existsAppointmentByPatientAndAgenda_StartAndAgenda_End(
          appointment.getPatient(), appointment.getAgenda().getStart(), appointment.getAgenda().getEnd());
    }

    @Override
    public Page<AppointmentDto> searchAppointments(SearchAppointmentsDto searchAppointmentsDto) {
        log.debug("Searching appointments with: {}", searchAppointmentsDto);
        return appointmentRepository.searchAppointmentsBy(
          super.getPageRequest(searchAppointmentsDto.getPageNumber(), searchAppointmentsDto.getPageSize(), "agenda.start"),
          searchAppointmentsDto.getDoctorId(),
          dateMapper.asTimestamp(searchAppointmentsDto.getStartingFrom()),
          dateMapper.asTimestamp(searchAppointmentsDto.getEndingAt()))
          .map(appointmentMapper::modelToDto);
    }

}
