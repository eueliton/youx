package com.medicina.schedule.controller;

import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.enums.Perfil;
import com.medicina.schedule.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para listar todos os usuários (somente ADMIN pode acessar)
    @Secured({"ROLE_ENFERMEIRO","ROLE_MEDICO"})
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para buscar um usuário pelo ID (somente ADMIN ou o próprio usuário pode acessar)
    @Secured({"ROLE_ENFERMEIRO","ROLE_MEDICO", "ROLE_PACIENTE"})
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable UUID id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // Endpoint para cadastrar um novo usuário (somente ADMIN ou ENFERMEIRO podem cadastrar)
    @Secured({"ROLE_ENFERMEIRO"})
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // Endpoint para atualizar informações de um usuário (somente ADMIN ou o próprio usuário podem atualizar)
    @Secured({"ROLE_ENFERMEIRO","ROLE_MEDICO", "ROLE_PACIENTE"})
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable UUID id, @RequestBody Usuario usuarioAtualizado) {
        Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);
        return ResponseEntity.ok(usuario);
    }

    // Endpoint para deletar um usuário (somente ADMIN pode deletar)
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_PACIENTE")
    @GetMapping("/all/medicos")
    public ResponseEntity<List<Usuario>> getAllMedicos() {
        List<Usuario> users = usuarioService.findByPerfil(Perfil.MEDICO);
        return ResponseEntity.ok(users);
    }


}