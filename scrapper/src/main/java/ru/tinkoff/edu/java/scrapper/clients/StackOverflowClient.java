package ru.tinkoff.edu.java.scrapper.clients;

import ru.tinkoff.edu.java.scrapper.web.dto.StackOverflowQuestion;

public interface StackOverflowClient {
    StackOverflowQuestion fetchQuestionInfo(String id);
}
