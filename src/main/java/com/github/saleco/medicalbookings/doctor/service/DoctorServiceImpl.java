package com.github.saleco.medicalbookings.doctor.service;

import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.mapper.DoctorMapper;
import com.github.saleco.medicalbookings.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public Page<DoctorDto> getDoctors(int page, int size) {
        return
          doctorRepository
            .findAll(PageRequest.of(page, size))
            .map(doctorMapper::modelToDto);
    }

    @Override
    public DoctorDto save(DoctorDto doctorDto) {
        return doctorMapper
          .modelToDto(doctorRepository.save(doctorMapper.dtoToModel(doctorDto)));
    }
}
