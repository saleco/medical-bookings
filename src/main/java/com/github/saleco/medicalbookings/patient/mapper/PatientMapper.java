package com.github.saleco.medicalbookings.patient.mapper;

import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.model.Patient;
import org.mapstruct.Mapper;

@Mapper
public interface PatientMapper {
    PatientDto modelToDto(Patient patient);
    Patient dtoToModel(PatientDto patientDto);

}
