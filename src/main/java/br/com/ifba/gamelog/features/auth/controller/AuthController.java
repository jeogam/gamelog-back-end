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

/**
 * Controlador responsável pela autenticação dos usuários.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * Endpoint para realizar o login do usuário.
     * * @param dto Dados de login (email e senha).
     * @return Token JWT se as credenciais estiverem corretas.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO dto) {
        // 1. Busca o usuário no banco pelo email (Lógica solicitada via Stream)
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(dto.email()))
                .findFirst()
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Verifica a senha (usando BCrypt)
        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. Gera o token JWT
        String token = tokenService.generateToken(usuario.getEmail());

        // 4. Retorna o token para o Front-end
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}