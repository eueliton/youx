package com.medicina.schedule.dto;

import com.medicina.schedule.entity.Paciente;
import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.enums.StatusConsulta;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitarConsultaDTO {

    @FutureOrPresent(message = "A data e hora devem ser no futuro ou presente.")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:m:s")
    private LocalDateTime dataHora;
    @NotBlank(message = "O motivo da consulta é obrigatório.")
    private String motivoConsulta;
    @NotNull(message = "O médico é obrigatório.")
    private UUID medicoId;
    @NotNull(message = "O médico é obrigatório.")
    private UUID pacienteId;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
}
