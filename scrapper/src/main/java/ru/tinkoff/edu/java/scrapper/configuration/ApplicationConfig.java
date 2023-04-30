package ru.tinkoff.edu.java.scrapper.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test,
                                Scheduler scheduler,
                                AccessType databaseAccessType,
                                RabbitData rabbitData) {
    public record Scheduler(Duration interval) {}

    public enum AccessType {
        JDBC,
        JPA,
        JOOQ
    }

    public record RabbitData(String username, String password, String exchange, String queue, String root){}
}
