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
        // Busca o usuário pelo Email
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElse(null);

        // Se não achar usuário, retorna 401
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Se a senha não bater, retorna 401
        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // --- ATUALIZAÇÃO ---
        // 1. Pega o papel do usuário (Ex: "ADMINISTRADOR")
        String role = usuario.getPapel().name();

        // 2. Gera o token
        String token = tokenService.generateToken(usuario.getEmail(), role);

        // 3. Retorna o Token E O PAPEL para o frontend
        return ResponseEntity.ok(new TokenResponseDTO(token, role));
    }
}