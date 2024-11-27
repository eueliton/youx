package com.medicina.schedule.service;

import com.medicina.schedule.dto.UsuarioDTO;
import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.enums.Perfil;
import com.medicina.schedule.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;  // Injetando o PasswordEncoder

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos os usuários
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar um usuário por ID
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    // Buscar um usuário por email
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Buscar um usuário por email
    public UsuarioDTO buscarPorEmailResume(String email) {
        return usuarioRepository.findByEmail(email).map(f-> mapUsuarioDTO(f))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private UsuarioDTO mapUsuarioDTO(Usuario user){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome(user.getNome());
        usuarioDTO.setRole(user.getPerfil().name());
        usuarioDTO.setId(user.getId());
        return  usuarioDTO;
    }

    // Cadastrar um novo usuário, com senha criptografada
    public Usuario cadastrar(Usuario usuario) {
        // Criptografando a senha antes de salvar no banco
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // Atualizar um usuário existente
    public Usuario atualizar(UUID id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);

        // Verificando se a senha foi alterada
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));  // Criptografando nova senha
        }

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setPerfil(usuarioAtualizado.getPerfil());

        return usuarioRepository.save(usuario);
    }

    // Deletar um usuário
    public void deletar(UUID id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public  List<Usuario> findByPerfil(Perfil perfil) {
        return usuarioRepository.findByPerfil(perfil);
    }



}

