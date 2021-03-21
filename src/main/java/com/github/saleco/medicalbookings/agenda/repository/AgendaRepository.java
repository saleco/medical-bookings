package com.github.saleco.medicalbookings.agenda.repository;

import com.github.saleco.medicalbookings.agenda.model.Agenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    @Query(value = "SELECT a FROM Agenda a WHERE upper(a.doctor.name) like upper(?1) and ((a.start between ?2 and ?3) or (a.end between ?2 and ?3)) and a.available = true")
    Page<Agenda> searchAvailabilityBy(Pageable pageable, String doctorName, Timestamp start, Timestamp end);

    @Query(value = "SELECT a FROM Agenda a WHERE a.doctor.id = ?1 and ((a.start between ?2 and ?3) or (a.end between ?2 and ?3)) and a.available = true")
    Page<Agenda> searchAvailabilityBy(Pageable pageable, Long doctorId, Timestamp start, Timestamp end);

}
