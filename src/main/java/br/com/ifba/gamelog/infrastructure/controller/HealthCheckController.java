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

    /**
     * ROTA LEVE (PING)
     * Use esta URL no UptimeRobot: https://.../api/health/ping
     * Objetivo: Responder rápido (HTTP 200) para manter o serviço "acordado"
     * sem depender da latência do banco de dados.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("PONG - Server is running");
    }

    /**
     * ROTA COMPLETA (DB CHECK)
     * Use esta URL no Postman/Navegador: https://.../api/health/db
     * Objetivo: Verificar se a conexão com o banco de dados está funcionando.
     */
    @GetMapping("/db")
    public ResponseEntity<String> checkDatabase() {
        try {
            long count = usuarioRepository.count();
            return ResponseEntity.ok("SISTEMA ONLINE - DB Conectado. Usuários: " + count);
        } catch (Exception e) {
            // É boa prática logar o erro para saber o motivo da falha no Render
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("ERRO DE BANCO: " + e.getMessage());
        }
    }
}