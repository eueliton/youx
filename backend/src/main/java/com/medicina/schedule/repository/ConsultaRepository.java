package com.medicina.schedule.repository;

import com.medicina.schedule.entity.Consulta;
import com.medicina.schedule.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


        // Buscar todas as consultas por paciente
        List<Consulta> findByPacienteId(UUID pacienteId);

        // Buscar todas as consultas por médico
        List<Consulta> findByMedicoId(UUID medicoId);


        // Verificar se já existe uma consulta no mesmo horário para o mesmo médico
        boolean existsByMedicoIdAndDataHora(UUID medicoId, LocalDateTime dataHora);

        // Buscar consulta por ID
        Optional<Consulta> findById(UUID consultaId);

}