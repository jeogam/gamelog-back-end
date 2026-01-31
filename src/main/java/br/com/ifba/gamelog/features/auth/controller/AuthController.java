package br.com.ifba.gamelog.features.auth.controller;

import br.com.ifba.gamelog.features.auth.dto.LoginDTO;
import br.com.ifba.gamelog.features.auth.dto.TokenResponseDTO;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável pelos endpoints de autenticação e autorização.
 * Gerencia o login de usuários e a emissão de tokens JWT.
 *
 * @author Jeovani Nunes
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e gestão de tokens")
public class AuthController {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * Realiza o login de um usuário no sistema.
     * <p>
     * Verifica se o email existe e se a senha confere com o hash armazenado.
     * Se as credenciais forem válidas, gera um token JWT contendo o ID e o Papel do usuário.
     * </p>
     *
     * @param dto Dados de login (email e senha).
     * @return ResponseEntity com o {@link TokenResponseDTO} contendo o token e o papel,
     * ou status 401 (Unauthorized) se as credenciais forem inválidas.
     */
    @Operation(summary = "Login de Usuário", description = "Autentica o usuário e retorna um token JWT válido.")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDTO dto) {
        // 1. Busca o usuário no banco pelo Email
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElse(null);

        // 2. Verifica se o usuário existe
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. Verifica se a senha bate com o hash (BCrypt)
        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 4. Extrai o papel para retorno (Ex: "ADMINISTRADOR" ou "USUARIO")
        String role = usuario.getPapel().name();

        // 5. Gera o Token JWT usando o serviço atualizado (passando o objeto Usuario completo)
        String token = tokenService.generateToken(usuario);

        // 6. Retorna o DTO com Token e Role para o Frontend armazenar
        return ResponseEntity.ok(new TokenResponseDTO(token, role));
    }
}