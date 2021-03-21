package com.github.saleco.medicalbookings.doctor.service;

import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;

public interface DoctorService {

    DoctorDto save(DoctorDto doctorDto);

    DoctorDto findById(Long id);
}
