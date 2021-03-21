package com.github.saleco.medicalbookings;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.CreateAppointmentDto;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import com.github.saleco.medicalbookings.exception.ValidationException;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PatientsIT extends BaseIT {


    @DisplayName("As a patient, I must be able to see the availability of the doctors and schedule an appointment for myself")
    @Test
    void getDoctorsAvailabilityAndScheduleAnAppointment() {

        Page<AgendaDto> agendaDtoPage = searchAvailability();
        assertDoctorAgenda(testDoctor, agendaDtoPage);

        AgendaDto agendaDto = agendaDtoPage.getContent().get(0);

        AppointmentDto testAppointment = createAppointment(agendaDto, testPatient);

        assertAppointment(testPatient, agendaDto, testAppointment);

        AgendaDto agendaDtoAfterAppointment = agendaService.findById(agendaDto.getId());
        assertThat(agendaDtoAfterAppointment.isAvailable()).isFalse();

    }

    @DisplayName("Create an Appointment with wrong Agenda")
    @Test
    void createAnAppointmentWithWrongAgenda() {
        assertThrows(NotFoundException.class, () -> appointmentManager.createAppointment(
          CreateAppointmentDto.builder().patientId(testPatient.getId()).agendaId(111111111111L).build()
        ));
    }

    @DisplayName("Create an Appointment with wrong Patient")
    @Test
    void createAnAppointmentWithWrongPatient() {
        Page<AgendaDto> agendaDtoPage = searchAvailability();
        AgendaDto agendaDto = agendaDtoPage.getContent().get(0);

        assertThrows(NotFoundException.class, () -> appointmentManager.createAppointment(
          CreateAppointmentDto.builder().patientId(111111111L).agendaId(agendaDto.getId()).build()
        ));
    }

    @DisplayName("Create an Appointment with Agenda not available")
    @Test
    void createAnAppointmentWithAgendaNotAvailable() {
        Page<AgendaDto> agendaDtoPage = searchAvailability();
        AgendaDto agendaDto = agendaDtoPage.getContent().get(0);
        agendaDto.setAvailable(false);
        agendaService.updateAgenda(agendaDto);

        assertThrows(ValidationException.class, () -> appointmentManager.createAppointment(
          CreateAppointmentDto.builder().patientId(testPatient.getId()).agendaId(agendaDto.getId()).build()
        ));
    }

    @DisplayName("Create an Appointment with Patient already has an appointment")
    @Test
    void createAnAppointmentWhenPatientAlreadyHasAnAppointment() {
        Page<AgendaDto> agendaDtoPage = searchAvailability();
        AgendaDto agendaDto = agendaDtoPage.getContent().get(0);

        //create first appointment
        appointmentManager.createAppointment(
          CreateAppointmentDto
            .builder()
            .agendaId(agendaDto.getId())
            .patientId(testPatient.getId())
            .build()
        );

        //create new agenda for new doctor with same start and end
        DoctorDto doctorDto2 = createDoctor();
        AgendaDto agendaDto2 = createAgenda(doctorDto2, START, END);

        //creates second appointment with same patient and same start and end
        assertThrows(ValidationException.class, () -> appointmentManager.createAppointment(
          CreateAppointmentDto.builder().patientId(testPatient.getId()).agendaId(agendaDto2.getId()).build()
        ));
    }

    private void assertAppointment(PatientDto testPatient, AgendaDto agendaDto, AppointmentDto testAppointment) {
        assertThat(testAppointment).isNotNull();
        assertThat(testAppointment.getAgenda().getId()).isEqualTo(agendaDto.getId());
        assertThat(testAppointment.getPatient().getId()).isEqualTo(testPatient.getId());
    }

    private void assertDoctorAgenda(DoctorDto testDoctor, Page<AgendaDto> agendaDtoPage) {
        assertThat(agendaDtoPage).isNotNull();
        assertThat(agendaDtoPage).isNotEmpty();

        AgendaDto agendaDto = agendaDtoPage.getContent().get(0);

        assertThat(agendaDto.isAvailable()).isTrue();
        assertThat(agendaDto.getStart()).isEqualTo(START);
        assertThat(agendaDto.getEnd()).isEqualTo(END);
        assertThat(agendaDto.getDoctor().getId()).isEqualTo(testDoctor.getId());
    }
}
