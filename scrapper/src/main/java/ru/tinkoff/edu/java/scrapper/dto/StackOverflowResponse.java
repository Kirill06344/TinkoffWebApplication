package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowResponse(
    @JsonProperty("answer_count")
    Integer answerCount,

    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivityDate
) {
}
