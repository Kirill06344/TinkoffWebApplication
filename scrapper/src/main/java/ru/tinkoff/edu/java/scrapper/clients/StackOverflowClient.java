package ru.tinkoff.edu.java.scrapper.clients;

import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;

import java.util.Optional;

public interface StackOverflowClient {
    Optional<StackOverflowResponse> fetchQuestionInfo(String id);
}
