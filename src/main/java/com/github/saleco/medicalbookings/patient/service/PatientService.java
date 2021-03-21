package com.github.saleco.medicalbookings.patient.service;

import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import org.springframework.data.domain.Page;

public interface PatientService {
    PatientDto save(PatientDto patientDto);

    PatientDto findById(Long id);
}
