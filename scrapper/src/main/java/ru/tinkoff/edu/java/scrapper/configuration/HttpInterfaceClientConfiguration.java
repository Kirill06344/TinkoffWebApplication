package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HttpInterfaceClientConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

}
