package br.com.ifba.gamelog.infrastructure.service;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    // Chave fixa e segura (pelo menos 32 bytes)
    // Em produção, mova para application.properties
    private static final String SECRET_STRING = "5v8y/B?E(H+MbQeThWmZq4t7w9z$C&F)J@NcRfUjXn2r5u8x/A%D*G-KaPdSgVkY";

    private Key secretKey;
    private static final long EXPIRATION_TIME = 864_000_000; // 10 dias

    @PostConstruct
    public void init() {
        // Converte a string segura em uma chave criptográfica real
        this.secretKey = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    }

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setIssuer("gamelog-api")
                .setSubject(usuario.getId().toString()) // ID no Subject
                .claim("email", usuario.getEmail())
                .claim("role", usuario.getPapel().name()) // Role no Claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token) {
        try {
            // Se o token estiver expirado ou a assinatura não bater, lança exceção
            return getClaims(token).getSubject();
        } catch (Exception e) {
            // Log para debug (opcional)
            System.err.println("Token inválido: " + e.getMessage());
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            return getClaims(token).get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Valida com a MESMA chave
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}