package com.github.saleco.medicalbookings;

import com.github.javafaker.Faker;
import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.dto.SearchDoctorsAvailabilityDto;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.CreateAppointmentDto;
import com.github.saleco.medicalbookings.appointment.dto.SearchAppointmentsDto;
import com.github.saleco.medicalbookings.appointment.manager.AppointmentManager;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.service.DoctorService;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.service.PatientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base Integration Tests class
 */
@SpringBootTest
public abstract class BaseIT {

    public static final OffsetDateTime START = OffsetDateTime.of(2021, 03, 21, 18, 00, 00, 00, ZoneOffset.UTC);
    public static final OffsetDateTime END = OffsetDateTime.of(2021, 03, 21, 19, 00, 00, 00, ZoneOffset.UTC);
    public static final LocalDateTime START_LOCAL_DATE = LocalDateTime.of(2021, 03, 24, 9, 0, 0, 0);
    public static final LocalDateTime END_LOCAL_DATE = LocalDateTime.of(2021, 03, 24, 18, 00, 00, 00);
    public static final String TEST_PATIENT_NAME = "Marie Johnson";

    @Autowired
    AgendaService agendaService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    AppointmentManager appointmentManager;

    protected DoctorDto testDoctor;
    protected PatientDto testPatient;
    protected AgendaDto testAgenda;
    private static final Faker faker = new Faker(LocaleContextHolder.getLocale());

    @BeforeEach
    void setUp() {
        this.testDoctor = createDoctor();
        this.testPatient = createPatient();
        this.testAgenda = createAgenda(testDoctor, START, END);
    }

    protected DoctorDto createDoctor() {
        return doctorService.save(DoctorDto.builder().name(faker.name().fullName()).build());
    }

    protected PatientDto createPatient() {
        return patientService.save(PatientDto.builder().name(TEST_PATIENT_NAME).build());
    }

    protected AgendaDto createAgenda(DoctorDto doctorDto, OffsetDateTime start, OffsetDateTime end) {
        return agendaService.createAgenda(
          AgendaDto
            .builder()
            .doctor(doctorDto)
            .start(start)
            .end(end)
            .available(true).build()
        );
    }

    protected Page<AgendaDto> searchAvailability() {
        return agendaService.getAvailability(
          SearchDoctorsAvailabilityDto
            .builder()
            .startingFrom(START)
            .endingAt(END)
            .doctorName(testDoctor.getName())
          .build());
    }

    protected  AppointmentDto createAppointment(AgendaDto agendaDto, PatientDto patientDto) {
        return appointmentManager.createAppointment(
          CreateAppointmentDto
            .builder()
            .agendaId(agendaDto.getId())
            .patientId(patientDto.getId())
            .build()
        );
    }

    protected Page<AppointmentDto> searchAppointments(Long doctorId) {
        return appointmentManager.searchAppointments(
          SearchAppointmentsDto
            .builder()
            .doctorId(doctorId)
            .startingFrom(START)
            .endingAt(END).build());
    }

}
