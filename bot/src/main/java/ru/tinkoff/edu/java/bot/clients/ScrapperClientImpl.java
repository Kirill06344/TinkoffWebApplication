package ru.tinkoff.edu.java.bot.clients;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;

import java.util.Optional;

public class ScrapperClientImpl implements ScrapperClient {

    private final WebClient webClient;

    public ScrapperClientImpl(String baseUrl) {
        webClient = setUpWebClient(baseUrl);
    }
    @Override
    public Optional<ListLinksResponse> getAllLinks(Long tgId) {
        return webClient.get()
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(tgId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createError)
                .bodyToMono(ListLinksResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .onErrorResume(WebClientRequestException.class, e -> Mono.empty())
                .blockOptional();
    }

    private WebClient setUpWebClient(String baseUrl) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
