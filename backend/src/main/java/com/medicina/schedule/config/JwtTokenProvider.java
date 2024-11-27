package com.medicina.schedule.config;

import com.medicina.schedule.entity.Usuario;
import com.medicina.schedule.repository.UsuarioRepository;
import com.medicina.schedule.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private final UsuarioRepository usuarioRepository;

    public JwtTokenProvider(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String generateToken(UserDetails userDetails) {

        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(userDetails.getUsername());
        Usuario user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", user.getId())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(role -> role.getAuthority())
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.get("id", String.class));
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Authentication getAuthentication(String token, UserDetails userDetails) {
        var authorities = userDetails.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}