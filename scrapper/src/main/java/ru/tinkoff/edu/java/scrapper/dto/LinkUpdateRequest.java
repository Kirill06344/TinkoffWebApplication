package ru.tinkoff.edu.java.scrapper.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record LinkUpdateRequest(
    @Min(0)
    @NotNull
    Long id,
    @URL
    @NotNull
    String url,
    @NotNull
    String description,
    @NotNull
    List<Long> tgChatIds
) {
}
