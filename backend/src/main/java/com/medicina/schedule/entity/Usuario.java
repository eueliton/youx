package com.medicina.schedule.entity;

import com.medicina.schedule.enums.Perfil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Email(message = "O email deve ser válido")
    @NotBlank(message = "O email não pode ser vazio")
    private String email;  // Email único para o login

    @Column(nullable = false)
    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;  // Senha criptografada

    @Column(nullable = false)
    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;  // Nome do usuário

    @Enumerated(EnumType.STRING)
    private Perfil perfil;


}