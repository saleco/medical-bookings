package com.github.saleco.medicalbookings.patient.service;

import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.mapper.PatientMapper;
import com.github.saleco.medicalbookings.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDto save(PatientDto patientDto) {
        return patientMapper
          .modelToDto(patientRepository.save(patientMapper.dtoToModel(patientDto)));
    }
}
