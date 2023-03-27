package ru.tinkoff.edu.java.scrapper.services;

import org.springframework.stereotype.Service;

import ru.tinkoff.edu.java.scrapper.clients.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.web.dto.StackOverflowQuestion;

@Service
public class StackOverflowService {
    private final StackOverflowClient client;

    public StackOverflowService(StackOverflowClient client) {
        this.client = client;
    }

    public StackOverflowQuestion getQuestionInfo(String id) {
        return client.fetchQuestionInfo(id);
    }
}
