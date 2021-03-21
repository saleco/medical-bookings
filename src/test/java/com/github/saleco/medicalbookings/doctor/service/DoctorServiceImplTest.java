package com.github.saleco.medicalbookings.doctor.service;

import com.github.saleco.medicalbookings.agenda.dto.AgendaDto;
import com.github.saleco.medicalbookings.agenda.model.Agenda;
import com.github.saleco.medicalbookings.appointment.dto.AppointmentDto;
import com.github.saleco.medicalbookings.appointment.model.Appointment;
import com.github.saleco.medicalbookings.doctor.dto.DoctorDto;
import com.github.saleco.medicalbookings.doctor.mapper.DoctorMapper;
import com.github.saleco.medicalbookings.doctor.model.Doctor;
import com.github.saleco.medicalbookings.doctor.repository.DoctorRepository;
import com.github.saleco.medicalbookings.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    DoctorMapper doctorMapper;

    @InjectMocks
    DoctorServiceImpl doctorService;

    @DisplayName("Given DoctorDto when save then should return DoctorDto")
    @Test
    void givenDoctorDtoWhenSaveThenShouldReturnDoctorDto() {
        Doctor doctor = mock(Doctor.class);
        DoctorDto doctorDto = mock(DoctorDto.class);
        given(doctorMapper.modelToDto(doctor)).willReturn(doctorDto);
        given(doctorMapper.dtoToModel(doctorDto)).willReturn(doctor);
        given(doctorRepository.save(doctor)).willReturn(doctor);

        DoctorDto doctorDtoReturned = doctorService.save(doctorDto);

        assertThat(doctorDtoReturned).isNotNull();

        then(doctorRepository).should(times(1)).save(doctor);
        then(doctorMapper).should(times(1)).modelToDto(doctor);
        then(doctorMapper).should(times(1)).dtoToModel(doctorDto);
        then(doctorRepository).shouldHaveNoMoreInteractions();
        then(doctorMapper).shouldHaveNoMoreInteractions();


    }

    @DisplayName("Given invalid Doctor Id when findById Then Should Throws NotFoundException")
    @Test
    void givenInvalidDoctorIdWhenFindByIdThenShouldThrowsNotFoundException() {
        given(doctorRepository.findById(1L)).willThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> doctorService.findById(1l));
    }

    @DisplayName("Given Doctor Id when findById Then Should Return DoctorDto")
    @Test
    void givenDoctorIdWhenFindByIdThenShouldReturnDoctorDto() {
        Doctor doctor = mock(Doctor.class);
        DoctorDto doctorDto = mock(DoctorDto.class);

        given(doctorRepository.findById(1L)).willReturn(Optional.of(doctor));
        given(doctorMapper.modelToDto(doctor)).willReturn(doctorDto);

        DoctorDto result = doctorService.findById(1L);

        assertThat(result).isNotNull();

        then(doctorRepository).should(times(1)).findById(anyLong());
        then(doctorMapper).should(times(1)).modelToDto(any(Doctor.class));

        then(doctorRepository).shouldHaveNoMoreInteractions();
        then(doctorMapper).shouldHaveNoMoreInteractions();

    }
}