package com.medicina.schedule.dto;

import com.medicina.schedule.entity.Paciente;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PacienteDTO {

    private UUID id;

    @NotBlank(message = "O nome do paciente é obrigatório.")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
    private String nome;

    @NotBlank(message = "O endereço é obrigatório.")
    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
    private String endereco;

    @Email(message = "O email deve ser válido")
    @NotBlank(message = "O email não pode ser vazio")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX.")
    private String telefone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    public Paciente convertToPaciente(){
        return Paciente.builder().nome(this.getNome()).email(this.getEmail()).telefone(this.getTelefone()).endereco(this.getEndereco()).dataNascimento(this.getDataNascimento()).build();
    }

}
