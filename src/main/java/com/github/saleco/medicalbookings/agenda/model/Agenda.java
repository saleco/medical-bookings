package com.github.saleco.medicalbookings.agenda.model;

import com.github.saleco.medicalbookings.doctor.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Doctor doctor;

    private Timestamp start;
    private Timestamp end;
    private boolean available;
}
