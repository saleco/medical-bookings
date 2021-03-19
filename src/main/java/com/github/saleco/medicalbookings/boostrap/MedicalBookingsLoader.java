package com.github.saleco.medicalbookings.boostrap;

import com.github.javafaker.Faker;
import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.service.AgendaService;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.service.DoctorService;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;

/**
 * Load inital data for Doctors
 */
@Component
@RequiredArgsConstructor
public class MedicalBookingsLoader implements CommandLineRunner {

    private static final Faker faker = new Faker(LocaleContextHolder.getLocale());
    public static final int INITAL_WORKING_HOUR = 9;
    public static final int FINAL_WORKING_HOUR = 19;
    public static final int LUNCH_TIME_HOUR = 12;
    public static final int MONTH_FINAL_DAY = 30;

    private final DoctorService doctorService;
    private final AgendaService agendaService;
    private final PatientService patientService;

    @Override
    public void run(String... args) throws Exception {

        //creates five doctors
        for (int i = 0; i < 5; i++) {
            DoctorDto doctorDto = doctorService.save(createDoctor());

            //creates an agenda for each working day until 30th of current month / each hour of the day from 9am to 6pm
            //not considering february
            //considering weekends and lunch time form 12pm to 1pm
            for(OffsetDateTime today = OffsetDateTime.now(); today.isBefore(OffsetDateTime.now().plusDays(MONTH_FINAL_DAY)); today = today.plusDays(1)) {
                for (int hour = INITAL_WORKING_HOUR; hour < FINAL_WORKING_HOUR; hour++) {
                    AgendaDto agendaDto = agendaService.save(createAgenda(doctorDto, today, hour));
                }
            }
        }

        //creates patients
        for (int i = 0; i < 20; i++) {
            PatientDto patientDto = patientService.save(createPatient());
        }
    }

    private PatientDto createPatient() {
        return PatientDto
          .builder()
          .name(faker.name().fullName())
          .build();
    }

    private AgendaDto createAgenda(DoctorDto doctorDto, OffsetDateTime today, int hour) {
        return AgendaDto
          .builder()
          .start(today.withHour(hour).withMinute(0).withSecond(0))
          .end(today.withHour(hour+1).withMinute(0).withSecond(0))
          .doctor(doctorDto)
          .available(isWeekend(today) || isLunchTime(hour) ? Boolean.FALSE : Boolean.TRUE) //lunch time
          .build();
    }

    private boolean isWeekend(OffsetDateTime today) {
        if(today == null) return false;
        return today.getDayOfWeek().equals(DayOfWeek.SATURDAY) || today.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private boolean isLunchTime(int hour) {
        return hour == LUNCH_TIME_HOUR;
    }

    private DoctorDto createDoctor() {
        return DoctorDto
          .builder()
          .name(faker.name().fullName())
          .build();
    }
}
