package com.github.saleco.medicalbookings;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.dto.UpdateDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import com.github.saleco.medicalbookings.exception.ValidationException;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DoctorsIT extends BaseIT {

    @DisplayName("As a doctor, I must be able to see the appointments that I have for a given time period.")
    @Test
    void getDoctorAppointmentsForAGivenTimePeriod() {
        Page<AgendaDto> agendaDtoPage = searchAvailability();

        AgendaDto agendaDto = agendaDtoPage.getContent().get(0);

        createAppointment(agendaDto, testPatient);

        //Check Doctors Appointments for a given period
        Page<AppointmentDto> appointmentDtos = searchAppointments(agendaDto.getDoctor().getId());

        assertAppointments(testPatient, testAgenda, appointmentDtos);

    }

    @DisplayName("As a doctor, I can set my self as unavailable for a specific time period. blocking any patients from scheduling an appointment for that period.")
    @Test
    void updateDoctorsAvailabilityInTheGivenPeriodThenCheckNoAgendaAvailable() {
        AgendaDto agendaDto = createAgenda(testDoctor,
          OffsetDateTime.of(2021, 03, 24, 12, 0, 0, 0, ZoneOffset.UTC),
          OffsetDateTime.of(2021, 03, 24, 13, 0, 0, 0, ZoneOffset.UTC));


        //update availabilty for the given period
        agendaService.updateAvailability(
          UpdateDoctorsAvailabilityDto
            .builder()
            .doctorId(testDoctor.getId())
            .startingFrom(START_LOCAL_DATE)
            .endingAt(END_LOCAL_DATE)
            .build());

        //check availability for given period / doctor and check there is none
        Page<AgendaDto> agendaDtoPage =
          agendaService.getAvailability(
          SearchDoctorsAvailabilityDto
            .builder()
            .doctorName(testDoctor.getName())
            .startingFrom(OffsetDateTime.of(2021, 03, 24, 9, 0, 0, 0, ZoneOffset.UTC))
            .endingAt(OffsetDateTime.of(2021, 03, 24, 18, 0, 0, 0, ZoneOffset.UTC))
            .build());

        assertThat(agendaDtoPage).isNotNull();
        assertThat(agendaDtoPage.getContent()).isEmpty();

    }

    @DisplayName("Setup unavailable when Doctor Id is wrong")
    @Test
    void updateAvailabilityWhenDoctorIdIsWrong() {
        assertThrows(NotFoundException.class, () -> agendaService.updateAvailability(
          UpdateDoctorsAvailabilityDto
            .builder()
            .doctorId(11111111L)
            .startingFrom(START_LOCAL_DATE)
            .endingAt(END_LOCAL_DATE)
            .build()));
    }

    @DisplayName("Setup unavailable when Doctor has appointments in the given period")
    @Test
    void updateAvailabilityWhenDoctorHasAppointmentsInTheGivenPeriod() {
        AgendaDto agendaDto = createAgenda(testDoctor,
          OffsetDateTime.of(2021, 03, 24, 9, 0, 0, 0, ZoneOffset.UTC),
          OffsetDateTime.of(2021, 03, 24, 10, 0, 0, 0, ZoneOffset.UTC));

        createAppointment(agendaDto, testPatient);

        //update availabilty with an appointment already setup
        assertThrows(ValidationException.class, () -> agendaService.updateAvailability(
          UpdateDoctorsAvailabilityDto
            .builder()
            .doctorId(testDoctor.getId())
            .startingFrom(START_LOCAL_DATE)
            .endingAt(END_LOCAL_DATE)
            .build()));
    }

    private void assertAppointments(PatientDto testPatient, AgendaDto testAgenda, Page<AppointmentDto> appointmentDtos) {
        assertThat(appointmentDtos).isNotNull();
        assertThat(appointmentDtos).isNotEmpty();
        assertThat(appointmentDtos).hasSize(1);

        AppointmentDto appointmentDto = appointmentDtos.getContent().get(0);
        assertThat(appointmentDto.getPatient().getId()).isEqualTo(testPatient.getId());
        assertThat(appointmentDto.getAgenda().getId()).isEqualTo(testAgenda.getId());
    }
}
