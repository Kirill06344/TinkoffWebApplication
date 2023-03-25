package ru.tinkoff.edu.java.scrapper.services;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.clients.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.configuration.StackOverflowProperties;
import ru.tinkoff.edu.java.scrapper.web.dto.StackOverflowResponse;

@Service
public class StackOverflowService {

    StackOverflowClient stackOverflowClient;

    StackOverflowProperties properties;

    public StackOverflowService(StackOverflowClient stackOverflowClient, StackOverflowProperties properties) {
        this.stackOverflowClient = stackOverflowClient;
        this.properties = properties;
    }

    public Mono<StackOverflowResponse> getQuestionInfoReactive(String id) {
        return stackOverflowClient.fetchQuestionInfo(id, properties.getSite());
    }
}
