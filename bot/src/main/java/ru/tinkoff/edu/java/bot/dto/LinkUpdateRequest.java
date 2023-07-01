package ru.tinkoff.edu.java.bot.dto;

import java.util.List;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
