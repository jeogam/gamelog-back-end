package br.com.ifba.gamelog.config.security;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.service.TokenAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTLoginFilter extends OncePerRequestFilter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final IUsuarioRepository userRepository;

    public JWTLoginFilter(TokenAuthenticationService tokenAuthenticationService, IUsuarioRepository userRepository) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Verifica token
        Optional<String> tokenOpt = extractToken(request);

        if (tokenOpt.isPresent()) {
            String email = tokenAuthenticationService.validateToken(tokenOpt.get());

            if (email != null) {
                // Busca usuário no banco (simulando UserDetails)
                // OBS: Seu UsuarioRepository pode precisar do método findByEmail
                Usuario user = userRepository.findAll().stream()
                        .filter(u -> u.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);

                if (user != null) {
                    // Autentica no Spring
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }
}