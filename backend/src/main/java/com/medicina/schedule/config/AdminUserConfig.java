package com.medicina.schedule.config;

import com.medicina.schedule.entity.Paciente;
import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.enums.Perfil;
import com.medicina.schedule.repository.PacienteRepository;
import com.medicina.schedule.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserConfig {

    @Bean
    public CommandLineRunner createAdminUser(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se o enfermeiro já existe
            if (usuarioRepository.findByEmail("admin@clinica.com").isEmpty()) {
                // Cria o usuário admin
                Usuario admin = new Usuario();
                admin.setId(null);
                admin.setNome("Administrador");
                admin.setEmail("admin@clinica.com");
                admin.setSenha(passwordEncoder.encode("admin123")); // Defina uma senha segura
                admin.setPerfil(Perfil.ENFERMEIRO);
                // Salva no banco de dados
                usuarioRepository.save(admin);

                System.out.println("Usuário administrador criado com sucesso!");
            } else {
                System.out.println("Usuário administrador já existe.");
            }
            // Verifica se o medico já existe
            if (usuarioRepository.findByEmail("medico@clinica.com").isEmpty()) {
                // Cria o usuário admin
                Usuario admin = new Usuario();
                admin.setId(null);
                admin.setNome("Medico");
                admin.setEmail("medico@clinica.com");
                admin.setSenha(passwordEncoder.encode("admin123")); // Defina uma senha segura
                admin.setPerfil(Perfil.MEDICO);
                // Salva no banco de dados
                usuarioRepository.save(admin);

                System.out.println("Usuário medico criado com sucesso!");
            } else {
                System.out.println("Usuário medico já existe.");
            }

            // Verifica se o medico já existe
            if (usuarioRepository.findByEmail("paciente@clinica.com").isEmpty()) {
                // Cria o usuário admin
                Usuario admin = new Usuario();
                admin.setId(null);
                admin.setNome("Paciente");
                admin.setEmail("paciente@clinica.com");
                admin.setSenha(passwordEncoder.encode("admin123")); // Defina uma senha segura
                admin.setPerfil(Perfil.PACIENTE);
                // Salva no banco de dados
                Usuario usuario = usuarioRepository.save(admin);


                Paciente paciente = new Paciente();
                paciente.setNome(admin.getNome());
                paciente.setEmail(admin.getEmail());
                paciente.setTelefone("(11) 94859-2525");
                paciente.setEndereco("Rua XPTO");
                paciente.setUsuario(usuario);
                pacienteRepository.save(paciente);
                System.out.println("Usuário paciente criado com sucesso!");
            } else {
                System.out.println("Usuário paciente já existe.");
            }
        };
    }
}