package com.medicina.schedule.service;

import com.medicina.schedule.dto.PacienteDTO;
import com.medicina.schedule.entity.*;
import com.medicina.schedule.enums.Perfil;
import com.medicina.schedule.repository.PacienteRepository;
import com.medicina.schedule.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioService usuarioService;

    // Cadastrar novo paciente
    public PacienteDTO cadastrarPaciente(PacienteDTO paciente) {
        if (pacienteRepository.existsByNomeAndDataNascimento(paciente.getNome(), paciente.getDataNascimento())) {
            throw new IllegalArgumentException("Paciente já cadastrado.");
        }
        Usuario user = new Usuario();
        user.setPerfil(Perfil.PACIENTE);
        user.setSenha("paciente123");
        user.setEmail(paciente.getEmail());
        user.setNome(paciente.getNome());
        usuarioService.cadastrar(user);
        return pacienteRepository.save(paciente.convertToPaciente()).convertToPacienteDTO();
    }

    // Listar todos os pacientes
    public List<Paciente> listarTodosPacientes() {
        return pacienteRepository.findAll();
    }

    // Buscar paciente por ID
    public Paciente buscarPorId(UUID id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
    }

    // Atualizar informações do paciente
    public Paciente atualizarPaciente(UUID id, Paciente pacienteAtualizado) {
        Paciente pacienteExistente = buscarPorId(id);
        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setDataNascimento(pacienteAtualizado.getDataNascimento());
        return pacienteRepository.save(pacienteExistente);
    }

    // Excluir paciente
    public void excluirPaciente(UUID id) {
        if (!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Paciente não encontrado.");
        }
        pacienteRepository.deleteById(id);
    }


    // Buscar paciente por ID
    public Paciente buscarPorUsuarioId(UUID usuarioId) {
        return pacienteRepository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
    }
}