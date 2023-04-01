package ru.tinkoff.edu.java.scrapper.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(
    @NotNull
    @URL
    String link
)
{}
