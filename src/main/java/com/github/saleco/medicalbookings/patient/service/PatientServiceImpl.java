package com.github.saleco.medicalbookings.patient.service;

import com.github.saleco.medicalbookings.exception.NotFoundException;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.mapper.PatientMapper;
import com.github.saleco.medicalbookings.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDto save(PatientDto patientDto) {
        log.debug("Creating patient {}", patientDto);
        return patientMapper
          .modelToDto(patientRepository.save(patientMapper.dtoToModel(patientDto)));
    }

    @Override
    public PatientDto findById(Long id) {
        log.debug("Searching patient with id {}", id);
        return patientRepository.findById(id)
          .map(patientMapper::modelToDto)
          .orElseThrow(() -> new NotFoundException(String.format("Patient %s not found.", id)));
    }

}
