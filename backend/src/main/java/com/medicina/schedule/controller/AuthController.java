package com.medicina.schedule.controller;

import com.medicina.schedule.config.JwtTokenProvider;
import com.medicina.schedule.dto.AuthRequest;
import com.medicina.schedule.dto.AuthResponse;
import com.medicina.schedule.dto.UsuarioDTO;
import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtService;

    private final UsuarioService userService;



    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtService, UsuarioService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);
        UsuarioDTO user = userService.buscarPorEmailResume(userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(jwtToken,user));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso.");
    }
}