package ru.tinkoff.edu.java.scrapper.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.web.dto.StackOverflowResponse;

@HttpExchange(url = "/questions/{id}", accept = "application/json")
public interface StackOverflowClient {

    @GetExchange
    Mono<StackOverflowResponse> fetchQuestionInfo(@PathVariable("id") String id, @RequestParam("site")String site);

}
