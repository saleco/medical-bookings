package com.github.saleco.medicalbookings.patient.repository;

import com.github.saleco.medicalbookings.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
