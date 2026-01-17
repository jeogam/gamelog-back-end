package br.com.ifba.gamelog.features.jogo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class RawgApiClient {

    private final WebClient webClient;
    private final String apiKey;

    // Define um timeout padrão para evitar travamentos
    private static final long API_TIMEOUT_SECONDS = 5;

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
     * Retorna os campos essenciais: ID Externo, Título, Capa e Ano (via released).
     *
     * @param query Termo de busca.
     * @return Lista de jogos externos ou lista vazia em caso de erro/timeout.
     */
    public Mono<List<RawgGameDetailResponse>> searchGames(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key", apiKey)
                        .queryParam("search", query)
                        .queryParam("page_size", 10) // Otimização
                        .build())
                .retrieve()
                .bodyToMono(RawgGameListResponse.class)
                .map(RawgGameListResponse::results)
                .timeout(Duration.ofSeconds(API_TIMEOUT_SECONDS)) // Trata Timeout
                .doOnError(e -> log.error("Erro na busca de jogos RAWG [query={}]: {}", query, e.getMessage()))
                .onErrorResume(e -> {
                    if (e instanceof TimeoutException) {
                        log.warn("Timeout ao buscar jogos na RAWG para o termo: {}", query);
                    }
                    // Retorna lista vazia para não quebrar o front-end
                    return Mono.just(List.of());
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
                .bodyToMono(RawgGameDetailResponse.class)
                .timeout(Duration.ofSeconds(API_TIMEOUT_SECONDS))
                .doOnError(e -> log.error("Erro ao buscar detalhe do jogo [id={}]: {}", idExterno, e.getMessage()));
    }
}