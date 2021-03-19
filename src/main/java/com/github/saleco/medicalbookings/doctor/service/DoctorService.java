package com.github.saleco.medicalbookings.doctor.service;

import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import org.springframework.data.domain.Page;

public interface DoctorService {

    Page<DoctorDto> getDoctors(int page, int size);
    DoctorDto save(DoctorDto doctorDto);

}
