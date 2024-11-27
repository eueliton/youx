package com.medicina.schedule.controller;

import com.medicina.schedule.entity.*;
import com.medicina.schedule.enums.Perfil;
import com.medicina.schedule.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultas/enfermeiro")
public class EnfermeiroController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteService pacienteService;

    // Aprovar agendamento de consulta
    @PutMapping("/{id}/aprovar")
    public Consulta aprovarConsulta(@PathVariable UUID id) {
        return consultaService.aprovarConsulta(id);
    }



}