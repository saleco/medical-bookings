package com.github.saleco.medicalbookings.agenda.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.dto.UpdateDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.mapper.AgendaMapper;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.agenda.repository.AgendaRepository;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.service.AppointmentService;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.mapper.DoctorMapper;
import com.github.saleco.medicalbookings.doctor.service.DoctorService;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import com.github.saleco.medicalbookings.exception.ValidationException;
import com.github.saleco.medicalbookings.mapper.DateMapper;
import com.github.saleco.medicalbookings.service.AbstractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AgendaServiceImpl extends AbstractService implements AgendaService {

    private final AgendaRepository agendaRepository;
    private final AgendaMapper agendaMapper;
    private final DateMapper dateMapper;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @Override
    public AgendaDto createAgenda(AgendaDto agendaDto) {
        log.debug("Creating agenda: {}", agendaDto);
        return agendaMapper
          .modelToDto(agendaRepository.save(agendaMapper.dtoToModel(agendaDto)));
    }

    @Override
    public Page<AgendaDto> getAvailability(SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto) {
        log.debug("Searching availability with: {}", searchDoctorsAvailabilityDto);
        return agendaRepository.searchAvailabilityBy(
          super.getPageRequest(searchDoctorsAvailabilityDto.getPageNumber(), searchDoctorsAvailabilityDto.getPageSize(), "doctor", "start"),
          buildNameLikeCriteria(searchDoctorsAvailabilityDto.getDoctorName()),
          dateMapper.asTimestamp(searchDoctorsAvailabilityDto.getStartingFrom()),
          dateMapper.asTimestamp(searchDoctorsAvailabilityDto.getEndingAt()))
          .map(agendaMapper::modelToDto);
    }

    @Override
    public AgendaDto findById(Long id) {
        log.debug("Searching agenda by id {}", id);
        return agendaRepository.findById(id)
          .map(agendaMapper::modelToDto)
          .orElseThrow(() -> new NotFoundException(String.format("Agenda %s not found.", id)));
    }

    @Override
    public void updateAgenda(AgendaDto agendaDto) {
        log.debug("Updating agenda {}", agendaDto);
        agendaRepository.save(agendaMapper.dtoToModel(agendaDto));
    }

    @Override
    public Page<AgendaDto> getAvailabilityByDoctorId(SearchDoctorsAvailabilityDto searchDoctorsAvailabilityDto, Long doctorId) {
        log.debug("Searching availability with: {} and doctor {}", searchDoctorsAvailabilityDto, doctorId);
        return Optional.ofNullable(agendaRepository.searchAvailabilityBy(
          super.getPageRequest(searchDoctorsAvailabilityDto.getPageNumber(), searchDoctorsAvailabilityDto.getPageSize(), "doctor", "start"),
          doctorId,
          dateMapper.asTimestamp(searchDoctorsAvailabilityDto.getStartingFrom()),
          dateMapper.asTimestamp(searchDoctorsAvailabilityDto.getEndingAt())))
          .orElse(Page.empty())
          .map(agendaMapper::modelToDto);
    }

    @Override
    public void updateAvailability(UpdateDoctorsAvailabilityDto updateDoctorsAvailabilityDto) {
        log.debug("Checking input if doctor exists");
        DoctorDto doctorDto = doctorService.findById(updateDoctorsAvailabilityDto.getDoctorId());

        log.debug("checking if has appointments for doctor {} between {} and {}",
          doctorDto, updateDoctorsAvailabilityDto.getStartingFrom(), updateDoctorsAvailabilityDto.getEndingAt());

        if(hasAppointmentsInPeriod(updateDoctorsAvailabilityDto, doctorDto)) {
           throw new ValidationException("The doctor already has appointments for the period.");
        }

        //if no appointment, create / update Agenda with availability false
        log.debug("Updating agenda availability for doctor {} between {} and {}",
          doctorDto, updateDoctorsAvailabilityDto.getStartingFrom(), updateDoctorsAvailabilityDto.getEndingAt());

        //check if has agenda available true in the period and remove it
        log.debug("Checking if has agenda available in the period.");
        Page<AgendaDto> agendaDtoPage = getAvailabilityByDoctorId(
          SearchDoctorsAvailabilityDto
          .builder()
            .doctorName(doctorDto.getName())
            .startingFrom(OffsetDateTime.of(updateDoctorsAvailabilityDto.getStartingFrom(), ZoneOffset.UTC))
            .endingAt(OffsetDateTime.of(updateDoctorsAvailabilityDto.getEndingAt(), ZoneOffset.UTC))
          .build(), doctorDto.getId());

        log.debug("Removing existing agendas for doctor {}", doctorDto);
        agendaRepository.deleteAll(agendaDtoPage.map(agendaMapper::dtoToModel));

        //include an agenda for the given period with available false
        log.debug("Creating agenda with no availability for doctor {}, from at {} to  {}",
          doctorDto, updateDoctorsAvailabilityDto.getStartingFrom(), updateDoctorsAvailabilityDto.getEndingAt());

        agendaRepository.save(
          Agenda
            .builder()
            .available(Boolean.FALSE)
            .doctor(doctorMapper.dtoToModel(doctorDto))
            .start(dateMapper.asTimestamp(OffsetDateTime.of(updateDoctorsAvailabilityDto.getStartingFrom(), ZoneOffset.UTC)))
            .end(dateMapper.asTimestamp(OffsetDateTime.of(updateDoctorsAvailabilityDto.getEndingAt(), ZoneOffset.UTC)))
            .build());

    }

    private boolean hasAppointmentsInPeriod(UpdateDoctorsAvailabilityDto updateDoctorsAvailabilityDto, DoctorDto doctorDto) {
        return appointmentService.searchAppointments(
          SearchAppointmentsDto
            .builder()
            .doctorId(doctorDto.getId())
            .startingFrom(OffsetDateTime.of(updateDoctorsAvailabilityDto.getStartingFrom(), ZoneOffset.UTC))
            .endingAt(OffsetDateTime.of(updateDoctorsAvailabilityDto.getEndingAt(), ZoneOffset.UTC))
            .build()
        ).hasContent();
    }

    private String buildNameLikeCriteria(String doctorName) {
        return "%name%".replace("name", doctorName != null ? doctorName : "");
    }
}
