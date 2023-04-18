package ru.tinkoff.edu.java.scrapper.clients;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.webjars.NotFoundException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidRepositoryInformation;

import java.util.Optional;

public class GitHubClientImpl implements GitHubClient {

    private final WebClient client;

    public GitHubClientImpl(String baseUrl) {
        client = setUpWebClient(baseUrl);
    }

    @Override
    public Optional<GitHubResponse> fetchRepositoryInfo(String owner, String repos) {
        return client.get()
                .uri("/repos/{owner}/{repos}", owner, repos).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GitHubResponse.class)
                .onErrorResume(error -> Mono.empty())
                .blockOptional();
    }

    private WebClient setUpWebClient(String baseUrl) {
        ExchangeFilterFunction errorResponseFilter = ExchangeFilterFunction
                .ofResponseProcessor(this::exchangeFilterResponseProcessor);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(errorResponseFilter)
                .build();
    }

    private Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.is4xxClientError()) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new InvalidRepositoryInformation(body)));
        }

        return Mono.just(response);
    }
}
