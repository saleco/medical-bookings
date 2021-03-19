package com.github.saleco.medicalbookings.doctor.mapper;

import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.model.Doctor;
import org.mapstruct.Mapper;

@Mapper
public interface DoctorMapper {
    DoctorDto modelToDto(Doctor doctor);
    Doctor dtoToModel(DoctorDto doctorDto);
}
