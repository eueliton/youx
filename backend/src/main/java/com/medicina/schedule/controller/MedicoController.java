package com.medicina.schedule.controller;

import com.medicina.schedule.entity.*;
import com.medicina.schedule.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private ConsultaService consultaService;



    // Aprovar agendamento de consulta
    @PutMapping("/{id}/aprovar")
    public Consulta aprovarConsulta(@PathVariable UUID id) {
        return consultaService.aprovarConsulta(id);
    }


    // Finalizar consulta
    @PutMapping("/{id}/finalizar")
    public Consulta finalizarConsulta(@PathVariable UUID id, @RequestBody String parecerFinal) {
        return consultaService.finalizarConsulta(id, parecerFinal);
    }

    // Visualizar apenas a agenda do médico
    @GetMapping
    public List<Consulta> listarMinhaAgenda(@AuthenticationPrincipal Usuario usuario) {
        return consultaService.listarConsultasPorMedico(usuario.getId());
    }



}