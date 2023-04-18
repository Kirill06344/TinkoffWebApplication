package ru.tinkoff.edu.java.scrapper.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubResponse(
    @JsonProperty("full_name")
    String fullName,
    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt
) {
}
