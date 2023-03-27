package ru.tinkoff.edu.java.scrapper.clients;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.tinkoff.edu.java.scrapper.web.dto.StackOverflowResponse;

public record StackOverflowQuestions(
    @JsonProperty("items")
    List<StackOverflowResponse> questions
) {
}
