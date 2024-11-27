package com.medicina.schedule.service;

import com.medicina.schedule.dto.SolicitarConsultaDTO;
import com.medicina.schedule.entity.Consulta;
import com.medicina.schedule.enums.Perfil;
import com.medicina.schedule.enums.StatusConsulta;
import com.medicina.schedule.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PacienteService pacienteService;

    // Método para criar uma nova consulta
    public Consulta cadastrarConsulta(SolicitarConsultaDTO consulta) {
        // Validando se a consulta já existe (mesmo médico, mesma data e hora)
        if (consultaRepository.existsByMedicoIdAndDataHora(consulta.getMedicoId(), consulta.getDataHora())) {
            throw new RuntimeException("Já existe uma consulta agendada para esse médico neste horário.");
        }
        Consulta consultaEntity = new Consulta();
        consultaEntity.setMotivoConsulta(consulta.getMotivoConsulta());
        consultaEntity.setDataHora(consulta.getDataHora());
        consultaEntity.setLatitude(consulta.getLatitude());
        consultaEntity.setLongitude(consulta.getLongitude());
        consultaEntity.setStatus(StatusConsulta.PENDENTE);
        consultaEntity.setMedico(usuarioService.buscarPorId(consulta.getMedicoId()));
        consultaEntity.setPaciente(pacienteService.buscarPorUsuarioId((usuarioService.buscarPorId(consulta.getPacienteId()).getId())));
        return consultaRepository.save(consultaEntity);
    }

    // Método para listar todas as consultas
    public List<Consulta> listarConsultas(String role, UUID userId) {
        if(Perfil.MEDICO.name().equals(role)){
            return listarConsultasPorMedico(userId);
        }else {
            return consultaRepository.findAll();
        }
    }

    // Método para listar consultas por paciente (apenas suas próprias consultas)
    public List<Consulta> listarConsultasPorPaciente(UUID pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }

    // Método para listar consultas por médico
    public List<Consulta> listarConsultasPorMedico(UUID medicoId) {
        return consultaRepository.findByMedicoId(medicoId);
    }

    // Método para aprovar uma consulta (médico ou enfermeiro pode aprovar)
    public Consulta aprovarConsulta(UUID consultaId) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(consultaId);
        if (consultaOpt.isEmpty()) {
            throw new RuntimeException("Consulta não encontrada.");
        }
        Consulta consulta = consultaOpt.get();
        if (consulta.getStatus() != StatusConsulta.PENDENTE) {
            throw new RuntimeException("Consulta já foi aprovada ou finalizada.");
        }
        consulta.setStatus(StatusConsulta.APROVADA);
        return consultaRepository.save(consulta);
    }

    // Método para finalizar uma consulta (apenas o médico pode finalizar)
    public Consulta finalizarConsulta(UUID consultaId, String parecerFinal) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(consultaId);
        if (consultaOpt.isEmpty()) {
            throw new RuntimeException("Consulta não encontrada.");
        }
        Consulta consulta = consultaOpt.get();
        if (consulta.getStatus() != StatusConsulta.APROVADA) {
            throw new RuntimeException("Consulta não foi aprovada ainda.");
        }
        consulta.setStatus(StatusConsulta.FINALIZADA);
        consulta.setParecerFinal(parecerFinal);
        consulta.setDataHoraFinalizada(LocalDateTime.now());
        return consultaRepository.save(consulta);
    }

    // Método para buscar uma consulta pelo id
    public Consulta buscarConsultaPorId(Long consultaId) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(consultaId);
        if (consultaOpt.isEmpty()) {
            throw new RuntimeException("Consulta não encontrada.");
        }
        return consultaOpt.get();
    }




}
