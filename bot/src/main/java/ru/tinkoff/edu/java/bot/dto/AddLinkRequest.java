package ru.tinkoff.edu.java.bot.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(
    @NotNull
    @URL
    String link
) {
}
