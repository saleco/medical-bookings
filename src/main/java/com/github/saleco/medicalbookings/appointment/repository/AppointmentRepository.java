package com.github.saleco.medicalbookings.appointment.repository;

import com.github.saleco.medicalbookings.appointment.model.Appointment;
import com.github.saleco.medicalbookings.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsAppointmentByPatientAndAgenda_StartAndAgenda_End(Patient patient, Timestamp start, Timestamp end);

    @Query(value = "SELECT a FROM Appointment a WHERE a.agenda.doctor.id = ?1 and ((a.agenda.start between ?2 and ?3) or (a.agenda.end between ?2 and ?3))")
    Page<Appointment> searchAppointmentsBy(Pageable pageable, Long doctorId, Timestamp start, Timestamp end);

}
