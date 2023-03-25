package ru.tinkoff.edu.java.scrapper.web.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestion(
    @JsonProperty("answer_count")
    Integer answerCount,

    @JsonProperty("creation_date")
    OffsetDateTime creationDate
) {
}
