package com.github.saleco.medicalbookings.appointment.manager;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.CreateAppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.service.AppointmentService;
import com.github.saleco.medicalbookings.exception.ValidationException;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointmentManager {

    private final AppointmentService appointmentService;
    private final AgendaService agendaService;
    private final PatientService patientService;

    @Transactional
    public AppointmentDto createAppointment(CreateAppointmentDto createAppointmentDto) {
        log.debug("Validate inputs for Agenda, Doctor and Patient");

        AgendaDto agendaDto = agendaService.findById(createAppointmentDto.getAgendaId());
        PatientDto patientDto = patientService.findById(createAppointmentDto.getPatientId());

        log.debug("Validates Agenda is really available");
        if(!agendaDto.isAvailable()) throw new ValidationException("Agenda is not available.");

        AppointmentDto appointmentDto = AppointmentDto
          .builder()
          .agenda(agendaDto)
          .patient(patientDto)
          .build();

        log.debug("Validates patient already has an appointment at the same time");
        if(appointmentService.appointmentAlreadyExists(appointmentDto)) {
            throw new ValidationException("Patient already has an appointment at this time.");
        }

        agendaDto.setAvailable(Boolean.FALSE);
        agendaService.updateAgenda(agendaDto);

        return appointmentService.createAppointment(appointmentDto);
    }

    public Page<AppointmentDto> searchAppointments(SearchAppointmentsDto searchAppointmentsDto) {
        return appointmentService.searchAppointments(searchAppointmentsDto);
    }
}
