package ru.tinkoff.edu.java.scrapper.web.dto;

import java.time.OffsetDateTime;

public record GitHubResponse(String fullName, OffsetDateTime updatedAt, OffsetDateTime createdAt) {
}
