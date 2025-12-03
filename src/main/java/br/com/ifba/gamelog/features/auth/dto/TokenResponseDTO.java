package br.com.ifba.gamelog.features.auth.dto;

public record TokenResponseDTO(String token, String tipo) {
    // Construtor auxiliar para facilitar
    public TokenResponseDTO(String token) {
        this(token, "Bearer");
    }
}