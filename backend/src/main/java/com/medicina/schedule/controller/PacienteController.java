package com.medicina.schedule.controller;


import com.medicina.schedule.config.JwtTokenProvider;
import com.medicina.schedule.dto.PacienteDTO;
import com.medicina.schedule.entity.Consulta;
import com.medicina.schedule.entity.Paciente;
import com.medicina.schedule.service.ConsultaService;
import com.medicina.schedule.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para gerenciamento de pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    private final ConsultaService consultaService;

    private final JwtTokenProvider jwtTokenProvider;

    public PacienteController(PacienteService pacienteService, ConsultaService consultaService, JwtTokenProvider jwtTokenProvider) {
        this.pacienteService = pacienteService;
        this.consultaService = consultaService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    @Operation(summary = "Cadastrar paciente", description = "Cadastra um novo paciente. Acesso restrito a Médicos e Enfermeiros.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public PacienteDTO cadastrarPaciente(@Valid @RequestBody PacienteDTO paciente) {
        return pacienteService.cadastrarPaciente(paciente);
    }

    @GetMapping
    @Operation(summary = "Listar pacientes", description = "Lista todos os pacientes. Acesso restrito a Médicos e Enfermeiros.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Paciente> listarPacientes() {
        return pacienteService.listarTodosPacientes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Busca um paciente específico pelo seu ID. Acesso restrito a Médicos e Enfermeiros.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public Paciente buscarPacientePorId(@PathVariable UUID id) {
        return pacienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizarPaciente(@PathVariable UUID id, @RequestBody @Valid Paciente paciente) {
        return ResponseEntity.ok(pacienteService.atualizarPaciente(id, paciente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir paciente", description = "Exclui um paciente pelo ID. Acesso restrito a Médicos e Enfermeiros.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public void excluirPaciente(@PathVariable UUID id) {
        pacienteService.excluirPaciente(id);
    }

    @GetMapping("/{id}/consultas")
    @PreAuthorize("hasRole('PACIENTE')")
    public List<Consulta> listarConsultaPaciente(@RequestHeader("Authorization") String token) {
        var parts = token.split(" ");
        Paciente paciente = pacienteService.buscarPorUsuarioId(jwtTokenProvider.getUserIdFromToken(parts[1]));
        return consultaService.listarConsultasPorPaciente(paciente.getId());
    }

}