package com.medicina.schedule.entity;

import com.medicina.schedule.enums.StatusConsulta;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "A data e hora da consulta são obrigatórias.")
    @FutureOrPresent(message = "A data e hora devem ser no futuro ou presente.")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:m:s")
    private LocalDateTime dataHora;

    @NotBlank(message = "O motivo da consulta é obrigatório.")
    private String motivoConsulta;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status é obrigatório.")
    private StatusConsulta status;

    @NotNull(message = "O paciente é obrigatório.")
    @ManyToOne
    private Paciente paciente;

    @NotNull(message = "O médico é obrigatório.")
    @ManyToOne
    private Usuario medico;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private String parecerFinal;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:m:s")
    private LocalDateTime dataHoraFinalizada;

}
