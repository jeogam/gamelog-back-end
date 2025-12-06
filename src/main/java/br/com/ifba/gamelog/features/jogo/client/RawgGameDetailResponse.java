package br.com.ifba.gamelog.features.jogo.client;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear os detalhes de um jogo da API RAWG.
 */
public record RawgGameDetailResponse(
        // Mapeia o ID da RAWG para o idExterno
        @JsonProperty("id")
        Long id,

        // Mapeia o título (name)
        @JsonProperty("name")
        String name,

        // Mapeia a URL da capa (background_image)
        @JsonProperty("background_image")
        String background_image,

        // A descrição detalhada (description_raw)
        @JsonProperty("description_raw")
        String description_raw,

        // Data de lançamento (formato YYYY-MM-DD)
        @JsonProperty("released")
        String released,

        // A API RAWG retorna plataformas e gêneros em estruturas complexas (objetos/arrays).
        // Para simplificar a primeira integração, usaremos String ou deixaremos de fora,
        // mas o mapeamento ideal exigiria DTOs aninhados (ex: List<PlatformResponse> platforms).
        // Por ora, vamos apenas definir os campos que serão simples de mapear.
        @JsonProperty("platforms")
        Object platforms, // Use Object ou crie DTOs aninhados

        @JsonProperty("genres")
        Object genres // Use Object ou crie DTOs aninhados

) {
    /**
     * Extrai o ano de lançamento do campo 'released' (YYYY-MM-DD).
     * @return O ano como Integer, ou null se a data for inválida.
     */
    public Integer getAnoLancamento() {
        if (released != null && released.length() >= 4) {
            try {
                // Pega os primeiros 4 caracteres (o ano)
                return Integer.parseInt(released.substring(0, 4));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}