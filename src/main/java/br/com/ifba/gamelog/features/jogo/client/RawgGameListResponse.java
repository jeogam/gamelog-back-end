package br.com.ifba.gamelog.features.jogo.client;

import java.util.List;

/**
 * DTO para mapear a resposta de listagem (search) da API RAWG.
 * Exemplo: GET /games?search=query
 *
 * @param results A lista de jogos retornados pela API.
 */
public record RawgGameListResponse(
        // O nome do campo no JSON da RAWG é 'results'
        List<RawgGameDetailResponse> results
) {}