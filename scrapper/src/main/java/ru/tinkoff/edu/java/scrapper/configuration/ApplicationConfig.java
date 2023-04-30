package ru.tinkoff.edu.java.scrapper.configuration;


import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test,
                                Scheduler scheduler,
                                AccessType databaseAccessType,
                                RabbitData rabbitData,
                                boolean useQueue) {
    public record Scheduler(Duration interval) {}

    public enum AccessType {
        JDBC,
        JPA,
        JOOQ
    }

    public record RabbitData(String username, String password, String exchange, String queue, String root){}

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
                .withRenderSchema(false)
                .withRenderFormatted(true)
                .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }
}
