package ru.tinkoff.edu.java.scrapper.clients;

import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowQuestions;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidQuestionInformation;

public class StackOverflowClientImpl implements StackOverflowClient {

    private static final String STACKOVERFLOW_SITE = "stackoverflow";
    private final WebClient client;

    public StackOverflowClientImpl(String baseUrl) {
        client = setUpWebClient(baseUrl);
    }

    @Override
    public Optional<StackOverflowResponse> fetchQuestionInfo(String id) {
        Optional<StackOverflowQuestions> response = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{ids}")
                .queryParam("site", STACKOVERFLOW_SITE)
                .build(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(StackOverflowQuestions.class)
            .onErrorResume(error -> Mono.empty())
            .blockOptional();

        return response.map(stackOverflowQuestions -> stackOverflowQuestions.questions().get(0));
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
