package br.com.ifba.gamelog.features.auth.controller;

import br.com.ifba.gamelog.features.auth.dto.LoginDTO;
import br.com.ifba.gamelog.features.auth.dto.TokenResponseDTO;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO dto) {
        // Busca otimizada pelo Email
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // ATUALIZAÇÃO: Passando o papel para o token
        String role = usuario.getPapel().name(); // Ex: "ADMINISTRADOR"
        String token = tokenService.generateToken(usuario.getEmail(), role);

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}