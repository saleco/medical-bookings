package com.github.saleco.medicalbookings.doctor.repository;

import com.github.saleco.medicalbookings.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
