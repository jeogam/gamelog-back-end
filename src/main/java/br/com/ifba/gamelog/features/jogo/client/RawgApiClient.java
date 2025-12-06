package br.com.ifba.gamelog.features.jogo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RawgApiClient {

    private final WebClient webClient;
    private final String apiKey;

    public RawgApiClient(
            WebClient.Builder webClientBuilder,
            @Value("${rawg.api.base-url}") String baseUrl,
            @Value("${rawg.api.key}") String apiKey
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    /**
     * Busca jogos na API RAWG.
     * @param query Termo de busca.
     * @return Lista de jogos externos.
     */
    public Mono<List<RawgGameDetailResponse>> searchGames(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key", apiKey) // Adiciona a chave de API
                        .queryParam("search", query)
                        .build())
                .retrieve()
                .bodyToMono(RawgGameListResponse.class) // Converte a resposta para o DTO de lista
                .map(RawgGameListResponse::results) // Pega apenas a lista de resultados
                .onErrorResume(e -> {
                    // Log de erro (opcional)
                    System.err.println("Erro ao buscar jogos na RAWG: " + e.getMessage());
                    return Mono.just(List.of()); // Retorna lista vazia em caso de falha
                });
    }

    /**
     * Busca detalhes de um jogo pelo ID Externo.
     */
    public Mono<RawgGameDetailResponse> getGameById(Long idExterno) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/{id}")
                        .queryParam("key", apiKey)
                        .build(idExterno))
                .retrieve()
                .bodyToMono(RawgGameDetailResponse.class);
    }
}