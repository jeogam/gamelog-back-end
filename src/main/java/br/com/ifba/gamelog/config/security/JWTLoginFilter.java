package br.com.ifba.gamelog.config.security;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.service.TokenService; // Usar TokenService!
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JWTLoginFilter extends OncePerRequestFilter {

    private final TokenService tokenService; // Corrigido para TokenService
    private final IUsuarioRepository userRepository;

    public JWTLoginFilter(TokenService tokenService, IUsuarioRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> tokenOpt = extractToken(request);

        if (tokenOpt.isPresent()) {
            String email = tokenService.validateToken(tokenOpt.get());

            if (email != null) {
                // Busca usuário no banco (simulando UserDetails)
                Usuario user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {
                    // ATUALIZAÇÃO: Cria a Authority baseada no Enum
                    // O Spring Security usa o padrão "ROLE_NOME"
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getPapel().name()));

                    // Autentica passando as authorities
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);

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