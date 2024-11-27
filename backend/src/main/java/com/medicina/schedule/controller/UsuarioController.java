package com.medicina.schedule.controller;

import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.enums.Perfil;
import com.medicina.schedule.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Endpoints para gerenciamento de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar usuários", description = "Lista todos usuarios. Acesso restrito a Médicos e Enfermeiros.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuários", description = "Busca usuários pelo ID",
              security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO') or hasRole('PACIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable UUID id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Cadastra usuário", description = "Cadastra usuário pelo ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ENFERMEIRO')")
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @Operation(summary = "Atualiza usuário", description = "Atualiza usuário pelo ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO') or hasRole('PACIENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable UUID id, @RequestBody Usuario usuarioAtualizado) {
        Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);
        return ResponseEntity.ok(usuario);
    }


    @Operation(summary = "Deleta usuário", description = "Deleta usuário pelo ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ENFERMEIRO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Obtém todos médicos", description = "Obtém todos médicos",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ENFERMEIRO')")
    @GetMapping("/all/medicos")
    public ResponseEntity<List<Usuario>> getAllMedicos() {
        List<Usuario> users = usuarioService.findByPerfil(Perfil.MEDICO);
        return ResponseEntity.ok(users);
    }


}