package com.github.saleco.medicalbookings.doctor.service;

import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.mapper.DoctorMapper;
import com.github.saleco.medicalbookings.doctor.repository.DoctorRepository;
import com.github.saleco.medicalbookings.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public Page<DoctorDto> getDoctors(int page, int size) {
        log.debug("Searching doctor by page {}, pageSize {}.", page, size);
        return
          doctorRepository
            .findAll(PageRequest.of(page, size))
            .map(doctorMapper::modelToDto);
    }

    @Override
    public DoctorDto save(DoctorDto doctorDto) {
        log.debug("Creating doctor {}", doctorDto);
        return doctorMapper
          .modelToDto(doctorRepository.save(doctorMapper.dtoToModel(doctorDto)));
    }

    @Override
    public DoctorDto findById(Long id) {
        log.debug("Searching doctor with id {}", id);
        return doctorRepository.findById(id)
          .map(doctorMapper::modelToDto)
          .orElseThrow(() -> new ValidationException("Doctor not found."));
    }
}
