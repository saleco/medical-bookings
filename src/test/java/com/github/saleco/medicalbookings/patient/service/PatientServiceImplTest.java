package com.github.saleco.medicalbookings.patient.service;

import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.model.Doctor;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import com.github.saleco.medicalbookings.patient.dto.PatientDto;
import com.github.saleco.medicalbookings.patient.mapper.PatientMapper;
import com.github.saleco.medicalbookings.patient.model.Patient;
import com.github.saleco.medicalbookings.patient.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {


    @Mock
    PatientRepository patientRepository;

    @Mock
    PatientMapper patientMapper;

    @InjectMocks
    PatientServiceImpl patientService;

    @DisplayName("Given PatientDto when save then should return PatientDto")
    @Test
    void givenPatientDtoWhenSaveThenShouldReturnPatientDto() {
        Patient patient = mock(Patient.class);
        PatientDto patientDto = mock(PatientDto.class);
        given(patientMapper.modelToDto(patient)).willReturn(patientDto);
        given(patientMapper.dtoToModel(patientDto)).willReturn(patient);
        given(patientRepository.save(patient)).willReturn(patient);

        PatientDto doctorDtoReturned = patientService.save(patientDto);

        assertThat(doctorDtoReturned).isNotNull();

        then(patientRepository).should(times(1)).save(patient);
        then(patientMapper).should(times(1)).modelToDto(patient);
        then(patientMapper).should(times(1)).dtoToModel(patientDto);
        then(patientRepository).shouldHaveNoMoreInteractions();
        then(patientMapper).shouldHaveNoMoreInteractions();


    }

    @DisplayName("Given invalid Patient Id when findById Then Should Throws NotFoundException")
    @Test
    void givenInvalidPatientIdWhenFindByIdThenShouldThrowsNotFoundException() {
        given(patientRepository.findById(1L)).willThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> patientService.findById(1l));
    }

    @DisplayName("Given Patient Id when findById Then Should Return PatientDto")
    @Test
    void givenPatientIdWhenFindByIdThenShouldReturnPatientDto() {
        Patient patient = mock(Patient.class);
        PatientDto patientDto = mock(PatientDto.class);

        given(patientRepository.findById(1L)).willReturn(Optional.of(patient));
        given(patientMapper.modelToDto(patient)).willReturn(patientDto);

        PatientDto result = patientService.findById(1L);

        assertThat(result).isNotNull();

        then(patientRepository).should(times(1)).findById(anyLong());
        then(patientMapper).should(times(1)).modelToDto(any(Patient.class));

        then(patientRepository).shouldHaveNoMoreInteractions();
        then(patientMapper).shouldHaveNoMoreInteractions();

    }

    @DisplayName("Given page and size when getPatients Then Should Return Page of Patient Dto")
    @Test
    void givenPageAndSizeWhenGetPatientsThenShouldReturnPageOfPatientDto() {
        Patient patient = mock(Patient.class);
        PatientDto patientDto = mock(PatientDto.class);

        given(patientRepository.findAll(PageRequest.of(0, 20))).willReturn(new PageImpl<>(Collections.singletonList(patient)));
        given(patientMapper.modelToDto(patient)).willReturn(patientDto);

        Page<PatientDto> patientDtos = patientService.getPatients(0, 20);

        assertThat(patientDtos).isNotNull();
        assertThat(patientDtos).hasSize(1);

        then(patientRepository).should(times(1)).findAll(any(PageRequest.class));
        then(patientMapper).should(times(1)).modelToDto(any(Patient.class));
        then(patientRepository).shouldHaveNoMoreInteractions();
        then(patientMapper).shouldHaveNoMoreInteractions();
    }
}