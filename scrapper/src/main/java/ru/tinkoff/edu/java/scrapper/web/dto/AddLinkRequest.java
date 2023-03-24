package ru.tinkoff.edu.java.scrapper.web.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotNull;

public record AddLinkRequest(
    @NotNull
    @URL
    String link
) {
}
