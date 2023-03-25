package ru.tinkoff.edu.java.scrapper.web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowResponse(
    @JsonProperty("items")
    List<StackOverflowQuestion> questions
) {
}
