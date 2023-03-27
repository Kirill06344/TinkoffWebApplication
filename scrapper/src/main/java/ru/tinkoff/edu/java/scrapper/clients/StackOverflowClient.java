package ru.tinkoff.edu.java.scrapper.clients;

import ru.tinkoff.edu.java.scrapper.web.dto.StackOverflowResponse;

public interface StackOverflowClient {
    StackOverflowResponse fetchQuestionInfo(String id);
}
