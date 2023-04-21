package ru.tinkoff.edu.java.scrapper.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

@Slf4j
public class BotClientImpl implements BotClient {

    private final WebClient webClient;

    public BotClientImpl(String baseUrl) {
        webClient = setUpWebClient(baseUrl);
    }

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        Mono<LinkUpdateRequest> updateRequest = Mono.just(request);
        var res = webClient.post()
                .uri("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateRequest, LinkUpdateRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(res);
    }

    private WebClient setUpWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


}
