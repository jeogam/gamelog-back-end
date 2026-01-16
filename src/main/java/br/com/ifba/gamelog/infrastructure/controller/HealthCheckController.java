package br.com.ifba.gamelog.infrastructure.controller;

import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthCheckController {

    private final IUsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        try {
            long count = usuarioRepository.count();
            return ResponseEntity.ok("SISTEMA ONLINE - DB Conectado. Usuários: " + count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ERRO DE BANCO");
        }
    }
}