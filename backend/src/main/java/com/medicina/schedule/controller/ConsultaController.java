package com.medicina.schedule.controller;

import com.medicina.schedule.config.JwtTokenProvider;
import com.medicina.schedule.dto.FinalizarConsultaDTO;
import com.medicina.schedule.dto.SolicitarConsultaDTO;
import com.medicina.schedule.entity.Consulta;
import com.medicina.schedule.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/consultas")
@Tag(name = "Consultas", description = "Endpoints para gerenciamento de consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    private final JwtTokenProvider jwtTokenProvider;

    public ConsultaController(ConsultaService consultaService, JwtTokenProvider jwtTokenProvider) {
        this.consultaService = consultaService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    @Operation(summary = "Agendar consulta", description = "Permite que pacientes solicitem uma nova consulta. Acesso restrito a Pacientes.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('PACIENTE')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou conflito de horário")
    })
    public ResponseEntity<?> cadastrarConsulta(@RequestBody @Valid SolicitarConsultaDTO request) {
        consultaService.cadastrarConsulta(request);
        return ResponseEntity.ok("Consulta cadastrada com sucesso!");
    }


    @GetMapping("/{role}")
    @Operation(summary = "Listar consultas", description = "Lista todas as consultas. Acesso restrito a Médicos e Enfermeiros.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public List<Consulta> listarConsultas(@PathVariable String role, @RequestHeader("Authorization") String token) {
        var parts = token.split(" ");
        var userId = jwtTokenProvider.getUserIdFromToken(parts[1]);
        return consultaService.listarConsultas(role, userId);
    }

    @PutMapping("/{id}/aprovar")
    @Operation(summary = "Aprovar consulta", description = "Permite que Médicos ou Enfermeiros aprovem consultas pendentes.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta aprovada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public Consulta aprovarConsulta(@PathVariable UUID id) {
        return consultaService.aprovarConsulta(id);
    }

    @PutMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar consulta", description = "Permite que Médicos finalizem consultas.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta finalizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public Consulta finalizarConsulta(@PathVariable UUID id, @RequestBody FinalizarConsultaDTO finalizarConsultaDTO) {
        return consultaService.finalizarConsulta(id, finalizarConsultaDTO.getParecerFinal());
    }


}