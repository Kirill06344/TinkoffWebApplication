package ru.tinkoff.edu.java.scrapper.clients;

import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;

public interface StackOverflowClient {
    StackOverflowResponse fetchQuestionInfo(String id);
}
