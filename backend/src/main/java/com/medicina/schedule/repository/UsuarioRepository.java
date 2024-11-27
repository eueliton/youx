package com.medicina.schedule.repository;


import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.enums.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Verificar se existe um usuário com o mesmo email
    boolean existsByEmail(String email);

    // Buscar usuário por email
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByPerfil(Perfil perfil);



}