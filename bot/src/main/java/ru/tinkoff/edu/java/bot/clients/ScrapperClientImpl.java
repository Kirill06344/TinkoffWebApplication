package ru.tinkoff.edu.java.bot.clients;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.dto.RemoveLinkRequest;

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

    @Override
    public Optional<LinkResponse> trackLink(Long tgId, String link) {
        Mono<AddLinkRequest> request = Mono.just(new AddLinkRequest(link));
        return webClient.post()
                .uri("/links")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Tg-Chat-Id", String.valueOf(tgId))
                .body(request, AddLinkRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createError)
                .bodyToMono(LinkResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .onErrorResume(WebClientRequestException.class, e -> Mono.empty())
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> Mono.empty())
                .blockOptional();
    }

    @Override
    public Optional<LinkResponse> untrackLink(Long tgId, String link) {
        Mono<RemoveLinkRequest> request = Mono.just(new RemoveLinkRequest(link));
        return webClient.method(HttpMethod.DELETE)
                .uri("/links")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Tg-Chat-Id", String.valueOf(tgId))
                .body(request, RemoveLinkRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createError)
                .bodyToMono(LinkResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .onErrorResume(WebClientRequestException.class, e -> Mono.empty())
                .onErrorResume(WebClientResponseException.BadRequest.class, e -> Mono.empty())
                .blockOptional();
    }

    private WebClient setUpWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
