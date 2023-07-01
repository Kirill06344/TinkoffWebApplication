package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, RabbitData rabbitData) {
    public record RabbitData(String username, String password, String queue) {
    }

}
