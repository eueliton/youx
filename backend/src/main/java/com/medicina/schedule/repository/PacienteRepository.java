package com.medicina.schedule.repository;

import com.medicina.schedule.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    // Verificar se existe um paciente com o mesmo nome e data de nascimento
    boolean existsByNomeAndDataNascimento(String nome, LocalDate dataNascimento);

    // Buscar paciente por nome
    Optional<Paciente> findByNome(String nome);
    Optional<Paciente> findByUsuario_Id(UUID usuarioId);
}