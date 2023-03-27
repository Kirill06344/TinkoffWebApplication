package ru.tinkoff.edu.java.scrapper.clients;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.web.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.web.exceptions.InvalidQuestionInformation;

public class StackOverflowClientImpl implements StackOverflowClient {

    private static final String STACKOVERFLOW_SITE = "stackoverflow";
    private final WebClient client;

    public StackOverflowClientImpl(String baseUrl) {
        client = setUpWebClient(baseUrl);
    }

    @Override
    public StackOverflowResponse fetchQuestionInfo(String id) {
        StackOverflowQuestions response = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{ids}")
                .queryParam("site", STACKOVERFLOW_SITE)
                .build(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(StackOverflowQuestions.class)
            .block();

        return response.questions().isEmpty() ? new StackOverflowResponse(0, OffsetDateTime.MIN)
            : response.questions().get(0);
    }

    protected WebClient setUpWebClient(String baseUrl) {
        ExchangeFilterFunction errorResponseFilter = ExchangeFilterFunction
            .ofResponseProcessor(this::exchangeFilterResponseProcessor);

        return WebClient.builder()
            .baseUrl(baseUrl)
            .filter(errorResponseFilter)
            .build();
    }

    protected Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.is4xxClientError()) {
            return response.bodyToMono(String.class).flatMap(body -> Mono.error(new InvalidQuestionInformation(body)));
        }

        return Mono.just(response);
    }

}
