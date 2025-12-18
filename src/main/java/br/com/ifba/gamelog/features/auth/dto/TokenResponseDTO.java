package br.com.ifba.gamelog.features.auth.dto;

public record TokenResponseDTO(String token, String tipo, String papel) {
    // Construtor auxiliar para facilitar
    public TokenResponseDTO(String token, String papel) {
        this(token, "Bearer", papel);
    }
}