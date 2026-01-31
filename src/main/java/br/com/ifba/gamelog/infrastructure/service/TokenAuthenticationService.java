package br.com.ifba.gamelog.infrastructure.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenAuthenticationService {

    private final TokenService tokenService;

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = recoverToken(request);

        if (token != null) {
            // 1. Valida o token e recupera o ID (Subject)
            String usuarioId = tokenService.validateToken(token);

            if (usuarioId != null) {
                // 2. Recupera o Papel (Role)
                String role = tokenService.getRoleFromToken(token);

                // 3. Monta a autoridade com prefixo ROLE_ (Obrigatório para o Spring)
                String authorityName = "ROLE_" + (role != null ? role : "USUARIO");

                // 4. Retorna a autenticação pronta
                return new UsernamePasswordAuthenticationToken(
                        usuarioId,
                        null,
                        List.of(new SimpleGrantedAuthority(authorityName))
                );
            }
        }
        return null;
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}